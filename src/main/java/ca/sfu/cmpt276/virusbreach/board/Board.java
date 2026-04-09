package ca.sfu.cmpt276.virusbreach.board;

import ca.sfu.cmpt276.virusbreach.Main;
import ca.sfu.cmpt276.virusbreach.VirusBreach;
import ca.sfu.cmpt276.virusbreach.board.entity.Data;
import ca.sfu.cmpt276.virusbreach.board.entity.Entity;
import ca.sfu.cmpt276.virusbreach.board.entity.SourceCode;
import ca.sfu.cmpt276.virusbreach.board.tile.TileType;
import ca.sfu.cmpt276.virusbreach.math.Position;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The game world.
 * Manages the floor/walls (tiles), entities, and their interactions.
 * Subject in the observer pattern.
 *
 * @see BoardObserver
 */
public class Board {
    /**
     * Executor that runs the game loop on a fixed interval.
     *
     * @see #start()
     */
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "Board Logic Thread"));

    /**
     * Shared random number generator
     */
    private final Random random = new Random();

    /**
     * The tiles in the board, indexed as tiles[y][x].
     *
     * @see #getTile(Position)
     * @see #setTile(Position, TileType)
     */
    private final TileType[][] tiles;

    /**
     * All entities on the board
     */
    private final List<Entity> entities = new ArrayList<>();
    private final Lock entityReadAddLock;
    private final Lock entityRemovalLock;
    
    /**
     * List of observers that are interested in this board's state.
     *
     * @see BoardObserver
     */
    private final List<BoardObserver> observers = new ArrayList<>();
    /**
     * The time this board has been played, in in-game ticks.
     *
     * @see VirusBreach#UPDATE_INTERVAL
     */
    private int timePlayed;
    /**
     * When {@code true}, the board will not update.
     */
    private volatile boolean paused = false;
    private int freezeTimer = 0;

    /**
     * Creates a board with the specified dimensions.
     *
     * @param tiles the tiles to use as floor/obstacles
     */
    public Board(TileType[][] tiles) {
        if (tiles.length == 0 || tiles[0].length == 0) throw new IllegalArgumentException("board cannot be empty");
        this.tiles = tiles;
        ReadWriteLock entityLock = new ReentrantReadWriteLock(true);
        this.entityReadAddLock = entityLock.readLock();
        this.entityRemovalLock = entityLock.writeLock();
    }

    /**
     * Locates the first entity that matches the predicate.
     *
     * @param predicate the function that determines if an entity is acceptable
     * @return the first entity that matches the predicate
     */
    public Entity getFirstEntityMatching(Predicate<Entity> predicate) {
        return this.iterateEntitiesYield(e -> predicate.test(e) ? e : null);
    }

    /**
     * Adds an entity to the board.
     *
     * @param entity the entity being added
     */
    public void addEntity(Entity entity) {
        if (entity.isRemoved()) return;

        this.entityReadAddLock.lock();
        this.entities.add(entity);
        this.entityReadAddLock.unlock();
        this.observers.forEach(observer -> observer.onEntityAdded(entity));
    }

    /**
     * Removes an entity from the board.
     *
     * @param entity the entity being removed
     */
    public void removeEntity(Entity entity) {
        if (entity.isRemoved()) return;

        entity.onRemove();
        this.observers.forEach(observer -> observer.onEntityRemoved(entity));
    }

    /**
     * Changes the type of the tile at the given position, notifying observers.
     *
     * @param position the position of the tile being changed
     * @param tile the type of the tile being placed
     */
    public void setTile(Position position, TileType tile) {
        this.tiles[position.y()][position.x()] = tile;
        this.observers.forEach(o -> o.onTileChanged(position, tile));
    }

    /**
     * Checks if the given position is within the board's bounds.
     *
     * @param pos the position to check
     * @return {@code true} if the position is within bounds, {@code false} otherwise
     */
    public boolean contains(Position pos) {
        return pos.x() >= 0
            && pos.x() < this.width()
            && pos.y() >= 0
            && pos.y() < this.height();
    }

    /**
     * Starts the main game loop, running once every {@link VirusBreach#UPDATE_INTERVAL} milliseconds.
     */
    public void start() {
        this.executor.scheduleAtFixedRate(this::tick, VirusBreach.UPDATE_INTERVAL, VirusBreach.UPDATE_INTERVAL, TimeUnit.MILLISECONDS);
    }

    /**
     * Pauses the game, halting all updates.
     *
     * @param paused whether the game should be paused
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * The main game loop.
     * Randomly spawns source code and updates all entities before scheduling a state synchronization to the display.
     */
    public void tick() {
        if (this.paused) return;
        try {
            this.timePlayed++;
            if (this.freezeTimer > 0) this.freezeTimer--;
            this.iterateEntities(Entity::tick);

            this.spawnSourceCode();

            this.entityRemovalLock.lock();
            this.entities.removeIf(Entity::isRemoved);
            this.entityRemovalLock.unlock();

            for (BoardObserver observer : this.observers) {
                observer.onUpdate();
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            RuntimeException exception = new RuntimeException(throwable);
            // don't kill javafx on tests
            if (!Main.testMode) {
                Platform.runLater(Platform::exit);
            }
            throw exception;
        }
    }

    /**
     * Randomly spawns a source code (bonus) entity on the board
     */
    void spawnSourceCode() {
        if (this.random.nextInt(0, 100) <= 3) {
            int sx = this.random.nextInt(0, this.width());
            int sy = this.random.nextInt(0, this.height());
            if (!this.getTile(new Position(sx, sy)).isSolid() && this.getEntitiesAt(new Position(sx, sy)).isEmpty()) {
                this.addEntity(new SourceCode(this, new Position(sx, sy), this.random.nextInt(40, 100)));
            }
        }
    }

    /**
     * Handles callbacks when an entity moves across the board.
     *
     * @param entity the entity that moved
     */
    public void handleEntityMoved(Entity entity) {
        this.getTile(entity.getPosition()).onStep(this, entity.getPosition(), entity);

        this.iterateEntities(e -> {
            if (e != entity) {
                if (e.getPosition().equals(entity.getPosition())) {
                    e.onCollideWith(entity);
                    entity.onCollideWith(e);
                }
            }
        });
    }

    /**
     * Returns all entities that exist at the given position.
     *
     * @param position the position of the entities
     * @return all entities that exist at the given position
     */
    public Collection<Entity> getEntitiesAt(Position position) {
        List<Entity> entities = new ArrayList<>();
        this.iterateEntities(entity -> {
            if (entity.getPosition().equals(position)) {
                entities.add(entity);
            }
        });
        return entities;
    }

    /**
     * The board's width (derived property)
     *
     * @return the number of tiles the board spans horizontally
     */
    public int width() {
        return this.tiles[0].length;
    }

    /**
     * The board's height (derived property)
     *
     * @return the number of tiles the board spans horizontally
     */
    public int height() {
        return this.tiles.length;
    }

    /**
     * {@return a shared random number generator}
     */
    public Random getRandom() {
        return random;
    }

    /**
     * Returns the tile at the given position.
     *
     * @param position the location of the tile to get
     * @return the tile at the given position
     */
    public TileType getTile(Position position) {
        return this.tiles[position.y()][position.x()];
    }

    /**
     * {@return the number of ticks that have occured since the game start}
     */
    public int getTimePlayed() {
        return timePlayed;
    }

    /**
     * {@return the number of ticks that firewalls will remain frozen for}
     */
    public int getFreezeTimer() {
        return freezeTimer;
    }

    /**
     * Freezes firewalls and prevents them from spreading for the specified number of in-game ticks.
     *
     * @param ticks how long to freeze firewalls for, in in-game ticks
     */
    public void freezeFirewallsFor(int ticks) {
        this.freezeTimer = ticks;
    }

    /**
     * Immediately stops the game logic thread, if running.
     */
    public void stop() {
        this.executor.close();
    }

    /**
     * Attaches the observer to this board, allowing it to receive notifications when the board's state changes.
     *
     * @param observer the observer to attach
     */
    public void attach(BoardObserver observer) {
        this.observers.add(observer);
    }

    /**
     * Detaches the observer from this board, preventing it from receiving notifications when the board's state changes.
     *
     * @param observer the observer to detach
     */
    public void detach(BoardObserver observer) {
        this.observers.remove(observer);
    }

    /**
     * Whether all the data on the board has been collected by the player.
     *
     * @return {@code true} if there is no data remaining, {@code false} otherwise
     */
    public boolean allDataCollected() {
        return this.iterateEntitiesYield(e -> e instanceof Data ? false : null) == null;
    }

    /**
     * Notifies observers that the game has been won.
     *
     * @param dataCollected the amount of data that the player collected before winning the game.
     */
    public void win(int dataCollected) {
        for (BoardObserver observer : this.observers) {
            observer.onWin(dataCollected);
        }
    }

    /**
     * Notifies observers that the game has been lost.
     */
    public void lose() {
        for (BoardObserver observer : this.observers) {
            observer.onLose();
        }
    }

    /**
     * Internal iterator pattern.
     * Runs a callback on every (non-removed) entity on the board.
     *
     * @param consumer callback that will be called on every entity on the board
     */
    public void iterateEntities(Consumer<Entity> consumer) {
        this.entityReadAddLock.lock();
        int size = this.entities.size();
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < size; i++) {
            Entity entity = this.entities.get(i);
            if (entity.isRemoved()) continue;
            consumer.accept(entity);
        }
        this.entityReadAddLock.unlock();
    }

    /**
     * Internal iterator pattern.
     * Runs a callback on every (non-removed) entity on the board, UNTIL the callback returns a non-{@code null} result.
     *
     * @param function the callback to call
     * @return the first non-{@code null} result from the function, otherwise {@code null}
     * @param <T> the type to yield
     */
    public <T> T iterateEntitiesYield(Function<Entity, T> function) {
        this.entityReadAddLock.lock();
        try {
            int size = this.entities.size();
            //noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < size; i++) {
                Entity entity = this.entities.get(i);
                if (entity.isRemoved()) continue;
                T result = function.apply(entity);
                if (result != null) return result;
            }
        } finally {
            this.entityReadAddLock.unlock();
        }
        return null;
    }
}

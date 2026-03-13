package ca.sfu.cmpt276.group15.board;

import ca.sfu.cmpt276.group15.HackingGame;
import ca.sfu.cmpt276.group15.board.entity.Data;
import ca.sfu.cmpt276.group15.board.entity.Entity;
import ca.sfu.cmpt276.group15.board.entity.SourceCode;
import ca.sfu.cmpt276.group15.board.tile.TileType;
import ca.sfu.cmpt276.group15.math.Position;
import javafx.application.Platform;

import java.io.Closeable;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class Board implements Closeable {
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private final Random random = new Random();
    private final TileType[][] tiles;
    private final int width;
    private final int height;

    private final List<Entity<?>> pendingEntities = new ArrayList<>();
    private final List<Entity<?>> entitiesPendingRemoval = new ArrayList<>();
    private final List<Entity<?>> entities = new ArrayList<>();
    private final List<BoardObserver> observers = new ArrayList<>();
    private int timePlayed;
    private volatile boolean paused = false;

    public Board(TileType[][] tiles) {
        this.tiles = tiles;
        this.width = tiles[0].length;
        this.height = tiles.length;
    }

    public List<Entity<?>> getEntities() {
        return entities;
    }

    public Entity<?> getFirstEntityMatching(Predicate<Entity<?>> predicate) {
        for (Entity<?> entity : this.entities) {
            if (predicate.test(entity)) {
                return entity;
            }
        }
        return null;
    }

    public void addEntity(Entity<?> entity) {
        this.pendingEntities.add(entity);
    }

    public void removeEntity(Entity<?> entity) {
        entity.onRemove();
        this.entitiesPendingRemoval.add(entity);
    }

    public void setTile(int x, int y, TileType tile) {
        this.tiles[y][x] = tile;
        this.observers.forEach(o -> o.onTileChanged(x, y, tile));
    }

    public TileType getTile(Position pos) {
        return this.tiles[pos.y()][pos.x()];
    }

    public boolean contains(Position pos) {
        return pos.x() >= 0
            && pos.x() < this.width
            && pos.y() >= 0
            && pos.y() < this.height;
    }

    public void start() {
        this.executor.scheduleAtFixedRate(this::tick, HackingGame.UPDATE_INTERVAL, HackingGame.UPDATE_INTERVAL, TimeUnit.MILLISECONDS);
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void tick() {
        if (this.paused) return;
        try {
            this.timePlayed++;
            for (Entity<?> entity : this.entities) {
                if (!entity.isRemoved()) entity.tick();
            }

            // random source code (bonus) reward spawning
            // TODO: source code not spawing inside walls
            if (this.random.nextInt(0, 100) <= 3) {
                this.addEntity(new SourceCode(this, this.random.nextInt(0, this.width), this.random.nextInt(0, this.height), this.random.nextInt(40, 100)));
            }

            for (Iterator<Entity<?>> iterator = this.entitiesPendingRemoval.reversed().iterator(); iterator.hasNext(); ) {
                Entity<?> entity = iterator.next();
                this.entities.remove(entity);
                this.observers.forEach(observer -> observer.onEntityRemoved(entity));
                iterator.remove();
            }

            for (Iterator<Entity<?>> iterator = this.pendingEntities.reversed().iterator(); iterator.hasNext(); ) {
                Entity<?> entity = iterator.next();
                this.entities.add(entity);
                this.observers.forEach(observer -> observer.onEntityAdded(entity));
                iterator.remove();
            }

            Platform.runLater(this::syncToView);
        } catch (Throwable throwable) {
            //TODO: remove try/catch and report handle exceptions properly
            throwable.printStackTrace();
            throw new RuntimeException(throwable);
        }
    }

    private void syncToView() {
        for (Entity<?> entity : this.entities) {
            entity.syncToView();
        }
    }

    public void entityMoved(Entity<?> entity) {
        this.getTile(entity.getPosition()).onStep(this, entity.getPosition(), entity);
        this.getTile(entity.getPrevPosition()).onLeave(this, entity.getPrevPosition(), entity);

        for (Entity<?> e : this.entities) {
            if (e != entity) {
                if (e.getPosition().equals(entity.getPosition())) {
                    e.onCollideWith(entity);
                    entity.onCollideWith(e);
                }
            }
        }
    }

    public Collection<Entity<?>> getEntitiesAt(Position pos) {
        return getEntitiesAt(pos.x(), pos.y());
    }

    public Collection<Entity<?>> getEntitiesAt(int x, int y) {
        List<Entity<?>> entities = new ArrayList<>();
        for (Entity<?> entity : this.entities) {
            if (entity.getPosition().x() == x && entity.getPosition().y() == y) {
                entities.add(entity);
            }
        }
        return entities;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Random getRandom() {
        return random;
    }

    public TileType getTile(int x, int y) {
        return this.tiles[y][x];
    }

    public int getTimePlayed() {
        return timePlayed;
    }

    @Override
    public void close() {
        this.executor.close();
    }

    public void attach(BoardObserver observer) {
        this.observers.add(observer);
    }

    public void detach(BoardObserver observer) {
        this.observers.remove(observer);
    }

    public boolean allDataCollected() {
        for (Entity<?> entity : this.entities) {
            if (entity instanceof Data && !entity.isRemoved()) return false;
        }
        return true;
    }

    public void win(int dataCollected) {
        for (BoardObserver observer : this.observers) {
            observer.onWin(dataCollected);
        }
    }

    public void lose() {
        for (BoardObserver observer : this.observers) {
            observer.onLose();
        }
    }
}

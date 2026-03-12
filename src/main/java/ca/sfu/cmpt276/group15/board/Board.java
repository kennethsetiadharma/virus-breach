package ca.sfu.cmpt276.group15.board;

import ca.sfu.cmpt276.group15.HackingGame;
import ca.sfu.cmpt276.group15.board.entity.Entity;
import ca.sfu.cmpt276.group15.board.entity.SourceCode;
import ca.sfu.cmpt276.group15.board.tile.TileType;
import ca.sfu.cmpt276.group15.math.Position;
import javafx.application.Platform;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Board implements Closeable {
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private final Random random = new Random();
    private final TileType[][] tiles;
    private final List<Entity> entities = new ArrayList<>();
    private int timePlayed;
    private final int width;
    private final int height;

    public Board(TileType[][] tiles) {
        this.tiles = tiles;
        this.width = tiles[0].length;
        this.height = tiles.length;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entity.onRemove();
        this.entities.remove(entity);
    }

    public void setTile(Position pos, TileType tile) {
        this.tiles[pos.y()][pos.x()] = tile;
    }
    public void setTile(int x, int y, TileType tile) {
        this.tiles[y][x] = tile;
    }

    public TileType getTile(Position pos) {
        return this.tiles[pos.y()][pos.x()];
    }

    public void start() {
        this.executor.scheduleAtFixedRate(this::tick, HackingGame.UPDATE_INTERVAL, HackingGame.UPDATE_INTERVAL, TimeUnit.MILLISECONDS);
    }

    public void tick() {
        this.timePlayed++;
        for (Entity entity : new ArrayList<>(this.entities)) {
            if (!entity.isRemoved()) entity.tick();
        }

        // random source code (bonus) reward spawning
        if (this.random.nextInt(0, 100) <= 3) {
            this.addEntity(new SourceCode(this, this.random.nextInt(40, this.width), this.random.nextInt(40, this.height), this.random.nextInt(40, 100)));
        }

        Platform.runLater(this::syncToView);
    }

    private void syncToView() {
        for (Entity entity : this.entities) {
            entity.syncToView();
        }
    }

    public void entityMoved(Entity entity) {
        this.getTile(entity.getPosition()).onStep(this, entity.getPosition(), entity);
        this.getTile(entity.getPrevPosition()).onLeave(this, entity.getPrevPosition(), entity);

        for (Entity e : this.entities) {
            if (e != entity) {
                if (e.getPosition().equals(entity.getPosition())) {
                    e.onCollideWith(entity);
                    entity.onCollideWith(e);
                }
            }
        }
    }

    public Collection<Entity> getEntitiesAt(Position pos) {
        return getEntitiesAt(pos.x(), pos.y());
    }

    public Collection<Entity> getEntitiesAt(int x, int y) {
        List<Entity> entities = new ArrayList<>();
        for (Entity entity : this.entities) {
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
}

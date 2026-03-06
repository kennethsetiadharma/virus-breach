package ca.sfu.cmpt276.group15.board;

import ca.sfu.cmpt276.group15.board.entity.Entity;
import ca.sfu.cmpt276.group15.board.tile.Tile;
import ca.sfu.cmpt276.group15.math.Position;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Tile[][] tiles;
    private final List<Entity> entities = new ArrayList<>();
    private int timePlayed;

    public Board(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entity.onRemove();
        this.entities.remove(entity);
    }

    public void tick() {
        this.timePlayed++;
        for (Entity entity : new ArrayList<>(this.entities)) {
            if (!entity.isRemoved()) entity.tick();
        }
    }

    public void render(double mouseX, double mouseY) {
    }

    public void setTile(Position pos, Tile tile) {
        this.tiles[pos.y()][pos.x()] = tile;
    }

    public Tile getTile(Position pos) {
        return this.tiles[pos.y()][pos.x()];
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
}

package ca.sfu.cmpt201.group15.board;

import ca.sfu.cmpt201.group15.board.entity.Entity;
import ca.sfu.cmpt201.group15.board.tile.Tile;
import ca.sfu.cmpt201.group15.math.Position;

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

    }

    public void removeEntity(int id) {
    }

    public void tick() {

    }

    public void render(double mouseX, double mouseY) {
    }

    public void setTile(Position pos, Tile tile) {

    }

    public void checkCollision(Entity entity) {

    }
}

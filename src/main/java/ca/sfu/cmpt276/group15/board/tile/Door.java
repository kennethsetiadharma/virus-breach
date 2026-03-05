package ca.sfu.cmpt276.group15.board.tile;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.entity.Entity;
import ca.sfu.cmpt276.group15.math.Position;

public class Door extends Tile {
    private final boolean open;

    public Door(boolean open) {
        this.open = open;
    }

    @Override
    public boolean canStep(Board board, Position pos, Entity entity) {
        return this.open;
    }

    @Override
    public void onStep(Board board, Position pos, Entity entity) {

    }

    @Override
    public void render(Board board, Position pos) {

    }
}

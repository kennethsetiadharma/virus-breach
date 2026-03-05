package ca.sfu.cmpt276.group15.board.tile;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.entity.Entity;
import ca.sfu.cmpt276.group15.math.Position;

public class Exit extends Tile {
    @Override
    public boolean canStep(Board board, Position pos, Entity entity) {
        return true;
    }

    @Override
    public void onStep(Board board, Position pos, Entity entity) {

    }

    @Override
    public void render(Board board, Position pos) {

    }
}

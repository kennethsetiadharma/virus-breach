package ca.sfu.cmpt276.group15.board.tile;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.entity.Entity;
import ca.sfu.cmpt276.group15.math.Position;

public abstract class Tile {
    protected Tile() {
    }

    public abstract boolean canStep(Board board, Position pos, Entity entity);

    public abstract void onStep(Board board, Position pos, Entity entity);
    public abstract void onLeave(Board board, Position pos, Entity entity);

    public abstract void render(Board board, Position pos);
}

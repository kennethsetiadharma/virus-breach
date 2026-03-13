package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.math.Position;

public abstract class Enemy extends Entity {
    public Enemy(Board board, int x, int y) {
        super(board, x, y);
    }

    public Enemy(Board board, Position position) {
        super(board, position);
    }
}

package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;

class DataTest extends CollectableTest {
    @Override
    protected Collectable createCollectable(Board board, int x, int y) {
        return new Data(board, x, y);
    }
}

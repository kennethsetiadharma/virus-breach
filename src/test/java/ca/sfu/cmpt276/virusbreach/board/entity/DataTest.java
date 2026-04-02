package ca.sfu.cmpt276.virusbreach.board.entity;

import ca.sfu.cmpt276.virusbreach.board.Board;

import ca.sfu.cmpt276.virusbreach.math.Position;

class DataTest extends CollectableTest {
    @Override
    protected Collectable createCollectable(Board board, Position position) {
        return new Data(board, position);
    }
}

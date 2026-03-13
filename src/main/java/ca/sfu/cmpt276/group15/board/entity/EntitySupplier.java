package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;

@FunctionalInterface
public interface EntitySupplier {
    Entity<?> create(Board board, int x, int y);
}

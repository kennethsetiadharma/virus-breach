package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;

public abstract class Reward extends Entity {
    private final int value;

    public Reward(Board board, int value) {
        super(board);
        this.value = value;
    }

    @Override
    public void onCollideWith(Entity entity) {
        super.onCollideWith(entity);
    }

    @Override
    public void tick() {
    }
}

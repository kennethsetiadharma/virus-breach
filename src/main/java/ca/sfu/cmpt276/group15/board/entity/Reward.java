package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;

public abstract class Reward extends Entity {
    private final int value;

    public Reward(Board board, int x, int y, int value) {
        super(board, x, y);
        this.value = value;
    }

    @Override
    public void onCollideWith(Entity entity) {
        super.onCollideWith(entity);
        if (entity instanceof Player player) {
            player.adjustData(this.value);
            this.board.removeEntity(this);
        }
    }
}

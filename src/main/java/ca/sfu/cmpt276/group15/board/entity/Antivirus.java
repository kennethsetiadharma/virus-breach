package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;

public class Antivirus extends Enemy {
    public Antivirus(Board board) {
        super(board);
    }

    @Override
    public void onCollideWith(Entity entity) {
        super.onCollideWith(entity);
        if (entity instanceof Player player) {
            this.board.removeEntity(player);
        }
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render() {

    }
}

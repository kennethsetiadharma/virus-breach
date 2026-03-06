package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;

public class Firewall extends Enemy {
    private final int damage;

    public Firewall(Board board, int damage) {
        super(board);
        this.damage = damage;
    }

    @Override
    public void onCollideWith(Entity entity) {
        super.onCollideWith(entity);
        if (entity instanceof Player) {
            ((Player) entity).adjustData(-this.damage);
        }
    }

    @Override
    public void render() {

    }
}

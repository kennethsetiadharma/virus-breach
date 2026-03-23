package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;

/**
 * Represents an entity that can be collected by a player to gain or lose data.
 */
public abstract class Collectable extends Entity {
    /**
     * Represents the amount of data that this collectable stores.
     * If negative, the player will lose data upon collection.
     */
    final int value;

    public Collectable(Board board, int x, int y, int value) {
        super(board, x, y);
        this.value = value;
    }

    /**
     * Called when a player collides with collectable.
     * Adjust player's data based on the collectable's specified {@code value}, then remove it from the board.
     */
    @Override
    public void onCollideWith(Entity entity) {
        super.onCollideWith(entity);
        if (entity instanceof Player player) {
            player.adjustData(this.value);
            this.board.removeEntity(this);
        }
    }
}

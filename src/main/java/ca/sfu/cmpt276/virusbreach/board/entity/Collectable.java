package ca.sfu.cmpt276.virusbreach.board.entity;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.math.Position;

/**
 * Represents an entity that can be collected by a player to gain or lose data.
 */
public abstract class Collectable extends Entity {
    /**
     * Represents the amount of data that this collectable stores.
     * If negative, the player will lose data upon collection.
     */
    final int value;

    /**
     * Constructs a new collectable entity on the board at the given position.
     *
     * @param board the board to spawn on
     * @param position the location to spawn at
     * @param value the amount of data that this collectable is worth
     */
    public Collectable(Board board, Position position, int value) {
        super(board, position);
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
            this.onCollectedByPlayer();
            player.adjustData(this.value);
            this.board.removeEntity(this);
        }
    }

    /**
     * Called to implement custom code, such as {@link DecryptionKey} and {@link FreezeToken}
     */
    protected void onCollectedByPlayer() {
    }
}

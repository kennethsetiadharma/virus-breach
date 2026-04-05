package ca.sfu.cmpt276.virusbreach.board;

import ca.sfu.cmpt276.virusbreach.board.entity.Entity;
import ca.sfu.cmpt276.virusbreach.board.tile.TileType;
import ca.sfu.cmpt276.virusbreach.math.Position;

/**
 * Observer participant in the observer pattern for a board.
 */
public interface BoardObserver {
    /**
     * Called when an entity is added to the board.
     *
     * @param entity the entity that was added to the board
     */
    default void onEntityAdded(Entity entity) {
    }

    /**
     * Called when an entity is removed from the board.
     *
     * @param entity the entity that was removed from the board
     */
    default void onEntityRemoved(Entity entity) {
    }

    /**
     * Called when the game has been won.
     *
     * @param dataCollected the amount of data the player collected during the game.
     *                      Greater than zero
     */
    default void onWin(int dataCollected) {
    }

    /**
     * Called when the game is lost (the player has died)
     */
    default void onLose() {
    }

    /**
     * Called after the board has completed an update.
     */
    default void onUpdate() {
    }

    /**
     * Called when a tile is updated on the board.
     *
     * @param position the position of the tile that was changed
     * @param tile the new tile that was placed at the given position
     */
    default void onTileChanged(Position position, TileType tile) {
    }
}

package ca.sfu.cmpt276.virusbreach.board.entity;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.board.tile.TileTypes;
import ca.sfu.cmpt276.virusbreach.math.Position;
import ca.sfu.cmpt276.virusbreach.ui.AudioManager;

/**
 * A key item found in the Storage Room.
 * Picking it up unlocks the Server Room door.
 */
public class DecryptionKey extends Collectable {
    private final Position doorToUnlock;

    /**
     * Constructs a new decryption key on the board at the given position.
     *
     * @param board the board this entity belongs to
     * @param position the position to spawn at
     * @param doorToUnlock the tile position of the Server Room door to open on pickup
     */
    public DecryptionKey(Board board, Position position, Position doorToUnlock) {
        super(board, position, 0);
        this.doorToUnlock = doorToUnlock;
    }

    /**
     * When the player walks onto this tile, sets their decryption key flag,
     * opens the Server Room door, plays a sound, and removes this entity.
     */
    @Override
    protected void onCollectedByPlayer() {
        this.board.setTile(this.doorToUnlock, TileTypes.OPEN_DOOR);
        AudioManager.play("bonus.wav");
    }
}

package ca.sfu.cmpt276.virusbreach.board.entity;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.board.tile.OpenDoor;
import ca.sfu.cmpt276.virusbreach.math.Position;
import ca.sfu.cmpt276.virusbreach.ui.AudioManager;

/**
 * A key item found in the Storage Room.
 * Picking it up unlocks the Server Room door.
 */
public class DecryptionKey extends Entity {
    private final Position doorToUnlock;

    /**
     * @param board         the board this entity belongs to
     * @param position the position to spawn at
     * @param doorToUnlock  the tile position of the Server Room door to open on pickup
     */
    public DecryptionKey(Board board, Position position, Position doorToUnlock) {
        super(board, position);
        this.doorToUnlock = doorToUnlock;
    }

    /**
     * When the player walks onto this tile, sets their decryption key flag,
     * opens the Server Room door, plays a sound, and removes this entity.
     *
     * @param entity the entity that collided with this key
     */
    @Override
    public void onCollideWith(Entity entity) {
        if (entity instanceof Player) {
            this.board.setTile(new Position(this.doorToUnlock.x(), this.doorToUnlock.y()), OpenDoor.INSTANCE);
            AudioManager.play("bonus.wav");
            this.board.removeEntity(this);
        }
    }
}

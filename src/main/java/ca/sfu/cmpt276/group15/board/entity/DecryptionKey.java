package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.tile.OpenDoor;
import ca.sfu.cmpt276.group15.math.Position;
import ca.sfu.cmpt276.group15.ui.AudioManager;
import ca.sfu.cmpt276.group15.ui.ResourceManager;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * A key item found in the Storage Room.
 * Picking it up unlocks the Server Room door.
 */
public class DecryptionKey extends Entity {
    private final Position doorToUnlock;

    /**
     * @param board         the board this entity belongs to
     * @param x             the x-coordinate to spawn at
     * @param y             the y-coordinate to spawn at
     * @param doorToUnlock  the tile position of the Server Room door to open on pickup
     */
    public DecryptionKey(Board board, int x, int y, Position doorToUnlock) {
        super(board, x, y);
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
            this.board.setTile(this.doorToUnlock.x(), this.doorToUnlock.y(), OpenDoor.INSTANCE);
            AudioManager.play("bonus.wav");
            this.board.removeEntity(this);
        }
    }

    /**
     * {@return a gold-coloured sprite node for this key}
     */
    @Override
    protected Node createRenderNode() {
        return ResourceManager.sprite("decryption_key.png", Color.GOLD);
    }
}

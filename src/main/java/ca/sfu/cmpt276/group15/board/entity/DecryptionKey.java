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

    public DecryptionKey(Board board, int x, int y, Position doorToUnlock) {
        super(board, x, y);
        this.doorToUnlock = doorToUnlock;
    }

    @Override
    public void onCollideWith(Entity entity) {
        if (entity instanceof Player player) {
            player.setHasDecryptionKey(true);
            this.board.setTile(doorToUnlock.x(), doorToUnlock.y(), OpenDoor.INSTANCE);
            AudioManager.play("bonus.wav");
            this.board.removeEntity(this);
        }
    }

    @Override
    protected Node createRenderNode() {
        return ResourceManager.sprite("decryption_key.png", Color.GOLD);
    }
}

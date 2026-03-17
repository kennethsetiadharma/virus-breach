package ca.sfu.cmpt276.group15.board.tile;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.math.Position;
import ca.sfu.cmpt276.group15.ui.ResourceManager;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * A locked door tile that blocks movement.
 * Replaced by {@link OpenDoor} when the player collects the Decryption Key.
 */
public class LockedDoor extends TileType {
    public static final TileType INSTANCE = new LockedDoor();

    /** Creates a solid locked door tile. */
    public LockedDoor() {
        super(true);
    }

    /**
     * {@return a crimson sprite node representing the locked door}
     */
    @Override
    public Node createNode(Board board, Position position) {
        return ResourceManager.sprite("door_locked.png", Color.CRIMSON);
    }
}

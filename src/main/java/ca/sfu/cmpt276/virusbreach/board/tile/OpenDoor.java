package ca.sfu.cmpt276.virusbreach.board.tile;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.math.Position;
import ca.sfu.cmpt276.virusbreach.ui.ResourceManager;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * An open door tile that allows movement through it.
 * Replaces {@link LockedDoor} when the player collects the Decryption Key.
 */
public class OpenDoor extends TileType {
    /**
     * The open door singleton instance.
     */
    public static final TileType INSTANCE = new OpenDoor();

    /**
     * Creates a passable open door tile.
     */
    private OpenDoor() {
        super(false);
    }

    /**
     * {@return a sprite node representing the open door}
     */
    @Override
    public Node createNode(Board board, Position position) {
        return ResourceManager.sprite("door_open.png", Color.LIMEGREEN);
    }
}

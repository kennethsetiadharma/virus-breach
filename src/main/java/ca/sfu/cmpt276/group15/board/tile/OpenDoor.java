package ca.sfu.cmpt276.group15.board.tile;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.math.Position;
import ca.sfu.cmpt276.group15.ui.ResourceManager;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * An open door tile that allows movement through it.
 * Replaces {@link LockedDoor} when the player collects the Decryption Key.
 */
public class OpenDoor extends TileType {
    public static final TileType INSTANCE = new OpenDoor();

    public OpenDoor() {
        super(false);
    }

    @Override
    public Node createNode(Board board, Position position) {
        return ResourceManager.sprite("open_door.png", Color.LIMEGREEN);
    }
}

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

    public LockedDoor() {
        super(true);
    }

    @Override
    public Node createNode(Board board, Position position) {
        return ResourceManager.sprite("locked_door.png", Color.CRIMSON);
    }
}

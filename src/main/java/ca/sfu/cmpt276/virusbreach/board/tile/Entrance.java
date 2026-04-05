package ca.sfu.cmpt276.virusbreach.board.tile;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.math.Position;
import ca.sfu.cmpt276.virusbreach.ui.ResourceManager;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * The player's spawning/starting tile.
 * There should only be one per board.
 */
public class Entrance extends TileType {
    /**
     * The entrance singleton.
     */
    public static final TileType INSTANCE = new Entrance();

    private Entrance() {
        super(false);
    }

    @Override
    public Node createNode(Board board, Position position) {
        Node node = ResourceManager.sprite("entrance.png", Color.LIGHTGREEN);
        node.setRotate(getRotation(board, position));
        return node;
    }

    private double getRotation(Board board, Position position) {
        if (position.x() == 0) {
            return 0.0;
        }
        if (position.x() == board.width() - 1) {
            return 180.0;
        }
        if (position.y() == 0) {
            return 90.0;
        }
        if (position.y() == board.height() - 1) {
            return -90.0;
        }
        return 0.0;
    }
}

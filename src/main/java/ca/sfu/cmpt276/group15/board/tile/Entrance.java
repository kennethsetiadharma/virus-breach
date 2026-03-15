package ca.sfu.cmpt276.group15.board.tile;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.math.Position;
import ca.sfu.cmpt276.group15.ui.ResourceManager;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * The player's spawning/starting tile.
 * There should only be one per board.
 */
public class Entrance extends TileType {
    public static final TileType INSTANCE = new Entrance();

    public Entrance() {
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

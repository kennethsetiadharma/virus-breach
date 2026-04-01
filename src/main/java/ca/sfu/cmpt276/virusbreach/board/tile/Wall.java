package ca.sfu.cmpt276.virusbreach.board.tile;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.math.Position;
import ca.sfu.cmpt276.virusbreach.ui.ResourceManager;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * A tile type that prevents movement.
 * Divides rooms and encloses the board.
 */
public class Wall extends TileType {
    public static final TileType INSTANCE = new Wall();

    public Wall() {
        super(true);
    }

    @Override
    public Node createNode(Board board, Position position) {
        return ResourceManager.sprite("wall.png", Color.DARKSLATEGRAY);
    }
}

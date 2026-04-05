package ca.sfu.cmpt276.virusbreach.board.tile;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.math.Position;
import ca.sfu.cmpt276.virusbreach.ui.ResourceManager;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * Most tiles in the game will be floor.
 * Has no special properties.
 */
public class Floor extends TileType {
    /**
     * The floor singleton.
     */
    public static final TileType INSTANCE = new Floor();

    private Floor() {
        super(false);
    }

    @Override
    public Node createNode(Board board, Position position) {
        return ResourceManager.sprite("floor.png", Color.GRAY);
    }
}

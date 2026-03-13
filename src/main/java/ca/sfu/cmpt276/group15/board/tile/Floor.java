package ca.sfu.cmpt276.group15.board.tile;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.math.Position;
import ca.sfu.cmpt276.group15.ui.ResourceManager;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class Floor extends TileType {
    public static final TileType INSTANCE = new Floor();

    public Floor() {
        super(false);
    }

    @Override
    public Node createNode(Board board, Position position) {
        return ResourceManager.sprite("floor.png", Color.GRAY);
    }
}

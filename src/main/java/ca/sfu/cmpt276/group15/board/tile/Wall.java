package ca.sfu.cmpt276.group15.board.tile;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.math.Position;
import ca.sfu.cmpt276.group15.ui.ResourceManager;
import javafx.scene.Node;
import javafx.scene.paint.Color;

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

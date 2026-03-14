package ca.sfu.cmpt276.group15.board.tile;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.entity.Entity;
import ca.sfu.cmpt276.group15.math.Position;
import ca.sfu.cmpt276.group15.ui.ResourceManager;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class Entrance extends TileType {

    public static final TileType INSTANCE = new Entrance();

    public Entrance() {
        super(false);
    }

    @Override
    public void onStep(Board board, Position position, Entity entity) {
        super.onStep(board, position, entity);
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
        if (position.x() == board.getWidth() - 1) {
            return 180.0;
        }
        if (position.y() == 0) {
            return 90.0;
        }
        if (position.y() == board.getHeight() - 1) {
            return -90.0;
        }
        return 0.0;
    }
}

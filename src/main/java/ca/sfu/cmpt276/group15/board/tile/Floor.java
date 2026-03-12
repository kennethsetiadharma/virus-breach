package ca.sfu.cmpt276.group15.board.tile;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.math.Position;
import ca.sfu.cmpt276.group15.ui.EntityNode;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class Floor extends TileType {
    public static final TileType INSTANCE = new Floor();

    public Floor() {
        super(false);
    }

    @Override
    public Node render(Board board, Position position) {
        Node sprite = EntityNode.sprite("floor.png", Color.GRAY);
        sprite.setTranslateX(position.x());
        sprite.setTranslateY(position.y());
        sprite.setTranslateZ(-1);
        return sprite;
    }
}

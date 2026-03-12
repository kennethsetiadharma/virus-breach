package ca.sfu.cmpt276.group15.board.tile;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.entity.Entity;
import ca.sfu.cmpt276.group15.math.Position;
import ca.sfu.cmpt276.group15.ui.EntityNode;
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
    public Node render(Board board, Position position) {
        Node sprite = EntityNode.sprite("entrance.png", Color.LIGHTGREEN);
        sprite.setTranslateX(position.x());
        sprite.setTranslateY(position.y());
        sprite.setTranslateZ(-1);
        return sprite;
    }
}

package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.math.Position;
import ca.sfu.cmpt276.group15.ui.EntityNode;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class Antivirus extends Enemy {
    public Antivirus(Board board, int x, int y) {
        super(board, x, y);
    }

    public Antivirus(Board board, Position position) {
        super(board, position);
    }

    @Override
    public void onCollideWith(Entity entity) {
        super.onCollideWith(entity);
        if (entity instanceof Player player) {
            this.board.removeEntity(player);
        }
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public Node renderNode() {
        return new EntityNode<>(this, "antivirus.png", Color.RED);
    }
}

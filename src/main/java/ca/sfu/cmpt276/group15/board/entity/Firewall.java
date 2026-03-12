package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.ui.EntityNode;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class Firewall extends Enemy {
    private final int damage;

    public Firewall(Board board, int x, int y) {
        super(board, x, y);
        this.damage = 1000;
    }

    @Override
    public void onCollideWith(Entity entity) {
        super.onCollideWith(entity);
        if (entity instanceof Player) {
            ((Player) entity).adjustData(-this.damage);
        }
    }

    @Override
    public Node renderNode() {
        return new EntityNode<>(this, "firewall.png", Color.ORANGE);
    }
}

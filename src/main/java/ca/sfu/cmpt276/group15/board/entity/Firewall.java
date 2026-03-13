package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.ui.ResourceManager;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class Firewall extends Enemy<Node> {
    private final int damage;

    public Firewall(Board board, int x, int y) {
        super(board, x, y);
        this.damage = 1000;
    }

    @Override
    public void onCollideWith(Entity<?> entity) {
        super.onCollideWith(entity);
        if (entity instanceof Player player) {
            player.adjustData(-this.damage);
            this.removeFromWorld();
        }
    }

    @Override
    public Node createRenderNode() {
        return ResourceManager.sprite("firewall.png", Color.ORANGE);
    }
}

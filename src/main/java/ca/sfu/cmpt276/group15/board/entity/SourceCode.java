package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.ui.EntityNode;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class SourceCode extends Reward {
    private int ttl;

    public SourceCode(Board board, int x, int y, int ttl) {
        super(board, x, y,250);
        this.ttl = ttl;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.ttl-- <= 0) {
            this.board.removeEntity(this);
        }
    }

    @Override
    public Node renderNode() {
        return new EntityNode<>(this, "sourcecode.png", Color.GREEN);
    }
}

package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.ui.EntityNode;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class Player extends Entity {
    private int dataCollected = 0;

    public Player(Board board, int x, int y) {
        super(board, x, y);
    }

    public void adjustData(int amount) {
        this.dataCollected += amount;
        if (this.dataCollected < 0) {
            this.dataCollected = 0;
        }
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public Node renderNode() {
        return new EntityNode<>(this, "player.png", Color.BLACK);
    }
}

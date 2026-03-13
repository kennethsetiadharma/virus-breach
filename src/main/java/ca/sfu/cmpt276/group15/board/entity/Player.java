package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.math.Direction;
import ca.sfu.cmpt276.group15.ui.ResourceManager;
import javafx.scene.Node;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {
    private final List<Direction> pendingMovement = new ArrayList<>(4);
    private int dataCollected = 0;

    public Player(Board board, int x, int y) {
        super(board, x, y);
    }

    public void adjustData(int amount) {
        this.dataCollected += amount;
        if (this.dataCollected < 0) {
            this.board.lose();
        }
    }

    public int getDataCollected() {
        return dataCollected;
    }

    @Override
    public void tick() {
        super.tick();

        for (Direction direction : this.pendingMovement.reversed()) {
            if (!this.board.getTile(this.getPosition().relative(direction)).isSolid()) {
                this.move(direction);
                break;
            }
        }
    }

    public void startMoving(Direction direction) {
        if (!this.pendingMovement.contains(direction)) this.pendingMovement.add(direction);
    }

    public void stopMoving(Direction direction) {
        this.pendingMovement.remove(direction);
    }

    @Override
    public Node createRenderNode() {
        return ResourceManager.sprite("player.png", Color.BLACK);
    }
}

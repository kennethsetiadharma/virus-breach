package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.math.Direction;
import ca.sfu.cmpt276.group15.ui.AnimatedNode;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity<AnimatedNode> {
    private static final String[] MOVING_SPRITES = {
        "player_moving1.png",
        "player_moving2.png"
    };

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
            var nextPosition = this.getPosition().relative(direction);
            if (this.board.contains(nextPosition) && !this.board.getTile(nextPosition).isSolid()) {
                this.move(direction);
                break;
            }
        }
    }

    @Override
    protected AnimatedNode createRenderNode() {
        return new AnimatedNode("player.png", MOVING_SPRITES);
    }

    @Override
    public void syncToView() {
        super.syncToView();
        this.renderNode.animate(this.getPosition(), this.getPrevPosition(), this.board.getTimePlayed());
    }

    public void startMoving(Direction direction) {
        if (!this.pendingMovement.contains(direction)) this.pendingMovement.add(direction);
    }

    public void stopMoving(Direction direction) {
        this.pendingMovement.remove(direction);
    }
}

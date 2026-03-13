package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class Player extends AnimatedEntity {
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

    public void startMoving(Direction direction) {
        if (!this.pendingMovement.contains(direction)) this.pendingMovement.add(direction);
    }

    public void stopMoving(Direction direction) {
        this.pendingMovement.remove(direction);
    }

    @Override
    protected String getIdleSpriteAsset() {
        return "player.png";
    }

    @Override
    protected String[] getMovingSpriteAssets() {
        return MOVING_SPRITES;
    }
}

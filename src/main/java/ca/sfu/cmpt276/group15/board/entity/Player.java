package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.math.Direction;
import ca.sfu.cmpt276.group15.ui.AnimatedNode;
import ca.sfu.cmpt276.group15.ui.menu.GameMenu;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * The "hacker" character, with movement controlled by the player.
 */
public class Player extends Entity {
    /**
     * List of movement directions to be attempted.
     * The player will move in the first direction that is possible from this list.
     */
    private final List<Direction> pendingMovement = new ArrayList<>(4);
    /**
     * The amount of data that the player has collected from the board.
     */
    private int dataCollected = 0;
    private AnimatedNode node;

    public Player(Board board, int x, int y) {
        super(board, x, y);
    }

    /**
     * Updates the amount of data that the player has collected (or lost) from the board, by the given amount.
     * Amount can be negative. If the player goes below zero data, the game is lost.
     *
     * @param amount the amount of data collected or lost
     */
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
    protected Node createRenderNode() {
        return this.node = new AnimatedNode("player.png", 2, "player_moving1.png", "player_moving2.png");
    }

    @Override
    public void syncToView() {
        super.syncToView();
        this.node.animate(this.getPosition(), this.getPrevPosition(), this.board.getTimePlayed());
    }

    /**
     * Enqueues movement in the given direction.
     * This direction will be favored over previously queued options.
     *
     * @param direction the direction to move in
     * @see GameMenu#onKeyPressed(KeyEvent)
     */
    public void startMoving(Direction direction) {
        if (!this.pendingMovement.contains(direction)) this.pendingMovement.add(direction);
    }

    /**
     * Stops movement in the given direction.
     *
     * @param direction the direction to stop moving in
     * @see GameMenu#onKeyReleased(KeyEvent)
     */
    public void stopMoving(Direction direction) {
        this.pendingMovement.remove(direction);
    }
}

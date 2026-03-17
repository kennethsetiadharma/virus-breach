package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * An invisible entity that keeps firewalls frozen for a fixed duration.
 * Removes itself and unfreezes firewalls when the timer expires.
 */
public class FreezeEffect extends Entity {
    private static final int FREEZE_TICKS = 100; // 100 ticks × 100ms = 10 seconds

    private int remainingTicks = FREEZE_TICKS;

    /**
     * Creates the freeze effect and immediately freezes all firewalls.
     *
     * @param board the board this entity belongs to
     */
    public FreezeEffect(Board board) {
        super(board, 0, 0);
        Firewall.setFrozen(true);
    }

    /**
     * Counts down the freeze timer each tick.
     * When it reaches zero, unfreezes all firewalls and removes this entity.
     */
    @Override
    public void tick() {
        super.tick();
        if (--this.remainingTicks <= 0) {
            Firewall.setFrozen(false);
            this.board.removeEntity(this);
        }
    }

    /**
     * {@return a zero-size transparent node — this entity is invisible}
     */
    @Override
    protected Node createRenderNode() {
        return new Rectangle(0, 0, Color.TRANSPARENT);
    }
}

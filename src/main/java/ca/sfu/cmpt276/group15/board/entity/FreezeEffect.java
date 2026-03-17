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

    public FreezeEffect(Board board) {
        super(board, 0, 0);
        Firewall.setFrozen(true);
    }

    @Override
    public void tick() {
        super.tick();
        if (--this.remainingTicks <= 0) {
            Firewall.setFrozen(false);
            this.board.removeEntity(this);
        }
    }

    @Override
    protected Node createRenderNode() {
        return new Rectangle(0, 0, Color.TRANSPARENT);
    }
}

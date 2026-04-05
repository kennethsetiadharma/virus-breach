package ca.sfu.cmpt276.virusbreach.board.entity;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.math.Position;
import ca.sfu.cmpt276.virusbreach.ui.AudioManager;

/**
 * A collectible found in the Server Room.
 * Freezes all firewall spreading for 10 seconds when collected.
 */
public class FreezeToken extends Collectable {
    /**
     * How long to freeze firewall spread for when collected, in ticks.
     */
    public static final int FREEZE_TIME = 100;

    /**
     * Constructs a new freeze token on the board at the given position
     *
     * @param board the board this entity belongs to
     * @param position the position to spawn at
     */
    public FreezeToken(Board board, Position position) {
        super(board, position, 0);
    }

    /**
     * When the player walks onto this tile, freezes firewall spread for a time,
     * plays a sound, and removes this entity.
     */
    @Override
    protected void onCollectedByPlayer() {
        this.board.freezeFirewallsFor(FREEZE_TIME);
        AudioManager.play("bonus.wav");
    }
}

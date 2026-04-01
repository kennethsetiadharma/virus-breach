package ca.sfu.cmpt276.virusbreach.board.entity;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.ui.AudioManager;
import ca.sfu.cmpt276.virusbreach.ui.ResourceManager;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * A collectible found in the Server Room.
 * Freezes all firewall spreading for 10 seconds when collected.
 */
public class FreezeToken extends Entity {

    public static final int FREEZE_TICKS = 100;

    /**
     * @param board the board this entity belongs to
     * @param x     the x-coordinate to spawn at
     * @param y     the y-coordinate to spawn at
     */
    public FreezeToken(Board board, int x, int y) {
        super(board, x, y);
    }

    /**
     * When the player walks onto this tile, freezes firewall spread for a time,
     * plays a sound, and removes this entity.
     *
     * @param entity the entity that collided with this token
     */
    @Override
    public void onCollideWith(Entity entity) {
        if (entity instanceof Player) {
            this.board.freezeFirewallsFor(FREEZE_TICKS);
            AudioManager.play("bonus.wav");
            this.board.removeEntity(this);
        }
    }

    /**
     * {@return a sprite node representing this token}
     */
    @Override
    protected Node createRenderNode() {
        return ResourceManager.sprite("freeze_token.png", Color.CORNFLOWERBLUE);
    }
}

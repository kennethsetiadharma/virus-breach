package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.ui.AudioManager;
import ca.sfu.cmpt276.group15.ui.ResourceManager;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * A collectible found in the Server Room.
 * Freezes all firewall spreading for 10 seconds when collected.
 */
public class FreezeToken extends Entity {
    public FreezeToken(Board board, int x, int y) {
        super(board, x, y);
    }

    @Override
    public void onCollideWith(Entity entity) {
        if (entity instanceof Player) {
            this.board.addEntity(new FreezeEffect(this.board));
            AudioManager.play("bonus.wav");
            this.board.removeEntity(this);
        }
    }

    @Override
    protected Node createRenderNode() {
        return ResourceManager.sprite("freeze_token.png", Color.CORNFLOWERBLUE);
    }
}

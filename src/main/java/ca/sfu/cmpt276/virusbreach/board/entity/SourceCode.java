package ca.sfu.cmpt276.virusbreach.board.entity;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.ui.AudioManager;
import ca.sfu.cmpt276.virusbreach.ui.ResourceManager;
import javafx.scene.Node;
import javafx.scene.paint.Color;

import ca.sfu.cmpt276.virusbreach.math.Position;

/**
 * A bonus reward that is spawned in at random during the game.
 * After a certain amount of time passes it will be removed from the game, even if the player has not collected it.
 */
public class SourceCode extends Collectable {
    /**
     * The entity will be removed from the board when this reaches zero.
     *
     * @see #tick()
     */
    private int ttl;

    public SourceCode(Board board, Position position, int ttl) {
        super(board, position, 250);
        this.ttl = ttl;
    }

    @Override
    public void onCollideWith(Entity entity) {
        if (entity instanceof Player) {
            AudioManager.play("bonus.wav");
        }
        super.onCollideWith(entity);
    }

    @Override
    public void tick() {
        super.tick();

        if (--this.ttl <= 0) {
            this.board.removeEntity(this);
        }
    }
}

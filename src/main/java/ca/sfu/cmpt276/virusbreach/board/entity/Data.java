package ca.sfu.cmpt276.virusbreach.board.entity;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.ui.AudioManager;
import ca.sfu.cmpt276.virusbreach.ui.ResourceManager;
import javafx.scene.Node;
import javafx.scene.paint.Color;

import ca.sfu.cmpt276.virusbreach.math.Position;

/**
 * Represents the "normal reward", providing score to the player when collected.
 */
public class Data extends Collectable {
    public Data(Board board, Position position) {
        super(board, position, 100);
    }

    @Override
    public void onCollideWith(Entity entity) {
        if (entity instanceof Player) {
            AudioManager.play("reward.wav");
        }
        super.onCollideWith(entity);
    }
}

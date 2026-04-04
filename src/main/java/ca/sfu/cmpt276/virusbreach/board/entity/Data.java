package ca.sfu.cmpt276.virusbreach.board.entity;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.math.Position;
import ca.sfu.cmpt276.virusbreach.ui.AudioManager;

/**
 * Represents the "normal reward", providing score to the player when collected.
 */
public class Data extends Collectable {
    /**
     * Constructs a new data collectable on the board at the given position.
     *
     * @param board the board to spawn on
     * @param position the location to spawn at
     */
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

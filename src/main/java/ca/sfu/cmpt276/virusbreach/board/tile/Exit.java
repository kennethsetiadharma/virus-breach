package ca.sfu.cmpt276.virusbreach.board.tile;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.board.entity.Entity;
import ca.sfu.cmpt276.virusbreach.board.entity.Player;
import ca.sfu.cmpt276.virusbreach.math.Position;

/**
 * The escape tile for the player.
 * If the player has collected all data, then they can win the game by stepping on this tile.
 */
public class Exit extends TileType {
    Exit() {
        super(false);
    }

    @Override
    public void onStep(Board board, Position position, Entity entity) {
        super.onStep(board, position, entity);
        if (entity instanceof Player player) {
            if (board.allDataCollected()) {
                board.win(player.getDataCollected());
            }
        }
    }
}

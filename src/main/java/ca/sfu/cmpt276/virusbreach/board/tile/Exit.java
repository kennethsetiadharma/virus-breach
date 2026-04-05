package ca.sfu.cmpt276.virusbreach.board.tile;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.board.entity.Entity;
import ca.sfu.cmpt276.virusbreach.board.entity.Player;
import ca.sfu.cmpt276.virusbreach.math.Position;
import ca.sfu.cmpt276.virusbreach.ui.ResourceManager;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * The escape tile for the player.
 * If the player has collected all data, then they can win the game by stepping on this tile.
 */
public class Exit extends TileType {
    /**
     * The exit singleton.
     */
    public static final TileType INSTANCE = new Exit();

    private Exit() {
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

    @Override
    public Node createNode(Board board, Position position) {
        return ResourceManager.sprite(getRespectiveAsset(board, position), Color.RED);
    }

    /**
     * Selects the image to be used to represent this tile, based on which edge of the board that it is located on.
     *
     * @param board    the board the tile is placed on
     * @param position the tile's location on the board
     * @return the location of the image file that should be used
     */
    private String getRespectiveAsset(Board board, Position position) {
        if (position.x() == 0) {
            return "exit_left.png";
        }
        if (position.x() == board.width() - 1) {
            return "exit_right.png";
        }
        if (position.y() == 0) {
            return "exit_up.png";
        }
        if (position.y() == board.height() - 1) {
            return "exit_down.png";
        }
        return "exit_left.png";
    }
}

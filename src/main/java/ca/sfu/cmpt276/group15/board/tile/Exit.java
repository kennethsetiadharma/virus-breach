package ca.sfu.cmpt276.group15.board.tile;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.entity.Entity;
import ca.sfu.cmpt276.group15.board.entity.Player;
import ca.sfu.cmpt276.group15.math.Position;
import ca.sfu.cmpt276.group15.ui.ResourceManager;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class Exit extends TileType {
    public static final TileType INSTANCE = new Exit();

    public Exit() {
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

    private String getRespectiveAsset(Board board, Position position) {
        if (position.x() == 0) {
            return "exit_left.png";
        }
        if (position.x() == board.getWidth() - 1) {
            return "exit_right.png";
        }
        if (position.y() == 0) {
            return "exit_up.png";
        }
        if (position.y() == board.getHeight() - 1) {
            return "exit_down.png";
        }
        return "exit_left.png";
    }
}

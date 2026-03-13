package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.ui.ResourceManager;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class Data extends Reward {
    public Data(Board board, int x, int y) {
        super(board, x, y, 100);
    }

    @Override
    public Node createRenderNode() {
        return ResourceManager.sprite("data.png", Color.GRAY);
    }
}

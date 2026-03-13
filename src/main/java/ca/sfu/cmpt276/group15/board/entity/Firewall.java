package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.ui.ResourceManager;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class Firewall extends Collectable {
    public Firewall(Board board, int x, int y) {
        super(board, x, y, -1000);
    }

    @Override
    public Node createRenderNode() {
        return ResourceManager.sprite("firewall.png", Color.ORANGE);
    }
}

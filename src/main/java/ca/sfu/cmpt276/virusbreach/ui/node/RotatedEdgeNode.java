package ca.sfu.cmpt276.virusbreach.ui.node;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.math.Position;
import ca.sfu.cmpt276.virusbreach.ui.ResourceManager;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.paint.Color;

/**
 * A render node that changes its rotation based on where it is placed on the board.
 */
public class RotatedEdgeNode extends Parent {
    /**
     * Creates a new render node with the wrapped node rotated based on where it is placed on the board.
     *
     * @param board the board being rendered
     * @param position the position of the object being rendered
     * @param node the node to actually render
     */
    public RotatedEdgeNode(Board board, Position position, Node node) {
        this.getChildren().add(node);
        this.setRotate(calculateRotation(board, position));
    }

    /**
     * Creates a new render node with the wrapped node rotated based on where it is placed on the board.
     *
     * @param asset the location of the sprite to render
     * @param color the colour to render if the sprite cannot be loaded
     * @return a factory that creates a rotated edge node
     */
    public static RenderNodeRegistry.TileRenderNodeFactory factory(String asset, Color color) {
        return (b, p) -> new RotatedEdgeNode(b, p, ResourceManager.sprite(asset, color));
    }

    static double calculateRotation(Board board, Position position) {
        if (position.x() == 0) return 0.0;
        else if (position.x() == board.width() - 1) return 180.0;
        else if (position.y() == 0) return 90.0;
        else if (position.y() == board.height() - 1) return -90.0;
        else return 0.0;
    }
}

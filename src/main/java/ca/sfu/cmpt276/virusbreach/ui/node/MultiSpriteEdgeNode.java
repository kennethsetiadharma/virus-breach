package ca.sfu.cmpt276.virusbreach.ui.node;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.math.Position;
import ca.sfu.cmpt276.virusbreach.ui.ResourceManager;
import javafx.scene.Parent;
import javafx.scene.paint.Color;

/**
 * A render node that changes its sprite based on what edge of the board it is on.
 */
public class MultiSpriteEdgeNode extends Parent {
    /**
     * Creates a render node that changes its sprite based on what edge of the board it is on.
     *
     * @param board the board being rendered
     * @param position the position of the object being rendered
     * @param asset the base asset name of the sprite
     * @param color the fallback color to render if any sprite fails to load
     */
    public MultiSpriteEdgeNode(Board board, Position position, String asset, Color color) {
        int i = asset.lastIndexOf('.');
        this.getChildren().add(ResourceManager.sprite(asset.substring(0, i) + '_' + calculateSuffix(board, position) + asset.substring(i), color));
    }

    /**
     * Creates a factory for a render node that changes its sprite based on what edge of the board it is on.
     *
     * @param baseName the base asset name of the sprites to render.
     *                 A direction will be appended to the sprite depending on the placement.
     * @param color the colour to render if the sprite cannot be loaded
     * @return a factory that creates a multi-sprite edge node
     */
    public static RenderNodeRegistry.TileRenderNodeFactory factory(String baseName, Color color) {
        return (b, p) -> new MultiSpriteEdgeNode(b, p, baseName, color);
    }

    static String calculateSuffix(Board board, Position position) {
        if (position.x() == 0) return "left";
        else if (position.x() == board.width() - 1) return "right";
        else if (position.y() == 0) return "up";
        else if (position.y() == board.height() - 1) return "down";
        else return "left";
    }
}

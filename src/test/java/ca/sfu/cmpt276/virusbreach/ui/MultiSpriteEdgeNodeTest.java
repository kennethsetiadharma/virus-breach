package ca.sfu.cmpt276.virusbreach.ui;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.board.TestHelper;
import ca.sfu.cmpt276.virusbreach.board.tile.TileTypes;
import ca.sfu.cmpt276.virusbreach.math.Position;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

class MultiSpriteEdgeNodeTest {
    @Test
    void loadCorrectAsset() {
        Board board = TestHelper.createEnclosedBoard(5, 5);

        assertSame(ResourceManager.loadSprite("exit_left.png"), imageAt(board, new Position(0, 2)));
        assertSame(ResourceManager.loadSprite("exit_right.png"), imageAt(board, new Position(4, 2)));
        assertSame(ResourceManager.loadSprite("exit_up.png"), imageAt(board, new Position(2, 0)));
        assertSame(ResourceManager.loadSprite("exit_down.png"), imageAt(board, new Position(2, 4)));
    }

    @Test
    void loadFallbackToLeftAsset() {
        Board board = TestHelper.createEnclosedBoard(5, 5);

        assertSame(ResourceManager.loadSprite("exit_left.png"), imageAt(board, new Position(2, 2)));
    }

    private static Image imageAt(Board board, Position position) {
        board.setTile(position, TileTypes.EXIT);
        ImageView node = (ImageView) ((Parent)RenderNodeRegistry.createRenderNodeForTile(board, position)).getChildrenUnmodifiable().getFirst();
        return node.getImage();
    }
}

package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.TestHelper;
import ca.sfu.cmpt276.group15.math.Direction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SourceCodeTest extends CollectableTest {
    @Override
    protected Collectable createCollectable(Board board, int x, int y) {
        return new SourceCode(board, x, y, 1000);
    }

    @Test
    void sourceCodeExpiry() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        SourceCode sourceCode = new SourceCode(board, 2, 2, 20);

        board.addEntity(sourceCode);

        for (int i = 0; i < 19; i++) {
            board.tick();
            assertFalse(sourceCode.isRemoved());
        }

        board.tick();
        assertTrue(sourceCode.isRemoved());
    }

    @Test
    void sourceCodeExpiredUncollectable() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        SourceCode collectable = new SourceCode(board, 2, 2, 20);
        Player player = new Player(board, 1, 2);

        board.addEntity(collectable);
        board.addEntity(player);

        for (int i = 0; i < 20; i++) {
            board.tick();
        }

        player.move(Direction.RIGHT);

        assertTrue(collectable.isRemoved());
        assertEquals(0, player.getDataCollected());
    }
}

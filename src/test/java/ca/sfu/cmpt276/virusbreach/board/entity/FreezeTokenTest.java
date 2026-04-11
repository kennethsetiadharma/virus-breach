package ca.sfu.cmpt276.virusbreach.board.entity;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.TestHelper;
import ca.sfu.cmpt276.virusbreach.math.Direction;
import ca.sfu.cmpt276.virusbreach.math.Position;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FreezeTokenTest {
    /**
     * Check that the player can collect a freeze token, and the board is frozen.
     */
    @Test
    void collectFreezeToken() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        FreezeToken token = new FreezeToken(board, new Position(2, 2));
        Player player = new Player(board, new Position(1, 2));
        board.addEntity(token);
        board.addEntity(player);

        player.move(Direction.RIGHT);

        assertTrue(token.isRemoved());
        assertNotEquals(0, board.getFreezeTimer());
    }

    /**
     * Ensure that the antivirus cannot collect a freeze token.
     */
    @Test
    void noAntivirusInteraction() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        FreezeToken token = new FreezeToken(board, new Position(2, 2));
        Antivirus antivirus = new Antivirus(board, new Position(1, 2));
        board.addEntity(token);
        board.addEntity(antivirus);

        antivirus.move(Direction.RIGHT);

        assertFalse(token.isRemoved());
        assertEquals(0, board.getFreezeTimer());
        assertEquals(2, board.getEntitiesAt(new Position(2, 2)).size());
    }
}

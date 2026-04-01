package ca.sfu.cmpt276.virusbreach.board.tile;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.board.TestHelper;
import ca.sfu.cmpt276.virusbreach.math.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntranceTest {
    @Test
    void correctRotation() {
        Board board = TestHelper.createEnclosedBoard(5, 5);

        assertEquals(0.0, Entrance.INSTANCE.createNode(board, new Position(0, 2)).getRotate());
        assertEquals(180.0, Entrance.INSTANCE.createNode(board, new Position(4, 2)).getRotate());
        assertEquals(90.0, Entrance.INSTANCE.createNode(board, new Position(2, 0)).getRotate());
        assertEquals(-90.0, Entrance.INSTANCE.createNode(board, new Position(2, 4)).getRotate());
        assertEquals(0.0, Entrance.INSTANCE.createNode(board, new Position(2, 2)).getRotate());
    }
}

package ca.sfu.cmpt276.group15.board.tile;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.TestHelper;
import ca.sfu.cmpt276.group15.math.Position;
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

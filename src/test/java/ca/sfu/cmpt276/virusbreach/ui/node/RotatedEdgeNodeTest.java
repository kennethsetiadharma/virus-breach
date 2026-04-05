package ca.sfu.cmpt276.virusbreach.ui.node;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.board.TestHelper;
import ca.sfu.cmpt276.virusbreach.math.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RotatedEdgeNodeTest {
    @Test
    void correctRotation() {
        Board board = TestHelper.createEnclosedBoard(5, 5);

        assertEquals(0.0, RotatedEdgeNode.calculateRotation(board, new Position(0, 2)));
        assertEquals(180.0, RotatedEdgeNode.calculateRotation(board, new Position(4, 2)));
        assertEquals(90.0, RotatedEdgeNode.calculateRotation(board, new Position(2, 0)));
        assertEquals(-90.0, RotatedEdgeNode.calculateRotation(board, new Position(2, 4)));
        assertEquals(0.0, RotatedEdgeNode.calculateRotation(board, new Position(2, 2)));
    }
}

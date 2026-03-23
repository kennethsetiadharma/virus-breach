package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.TestHelper;
import ca.sfu.cmpt276.group15.math.Direction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

abstract class CollectableTest {
    protected abstract Collectable createCollectable(Board board, int x, int y);

    @Test
    void playerCollection() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Collectable collectable = this.createCollectable(board, 2, 2);
        Player player = new Player(board, 1, 2);

        board.addEntity(collectable);
        board.addEntity(player);

        player.move(Direction.RIGHT);

        assertTrue(collectable.isRemoved());
        assertEquals(collectable.value, player.getDataCollected());
    }

    @Test
    void noAntivirusInteraction() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Collectable collectable = this.createCollectable(board, 2, 2);
        Antivirus antivirus = new Antivirus(board, 1, 2);

        board.addEntity(collectable);
        board.addEntity(antivirus);

        antivirus.move(Direction.RIGHT);

        assertFalse(collectable.isRemoved());
        assertEquals(2, board.getEntitiesAt(2, 2).size());
    }
}

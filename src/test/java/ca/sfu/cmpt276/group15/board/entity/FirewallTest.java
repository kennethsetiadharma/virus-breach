package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.BoardObserver;
import ca.sfu.cmpt276.group15.board.TestHelper;
import ca.sfu.cmpt276.group15.math.Direction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FirewallTest extends CollectableTest implements BoardObserver{
    int gamesLost = 0;

    @Override
    protected Collectable createCollectable(Board board, int x, int y) {
        return new Firewall(board, x, y);
    }

    @Test
    void canKillPlayer() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Collectable collectable = this.createCollectable(board, 2, 2);
        Player player = new Player(board, 1, 2);

        board.attach(this);
        board.addEntity(collectable);
        board.addEntity(player);

        player.move(Direction.RIGHT);

        assertTrue(collectable.isRemoved());
        assertEquals(collectable.value, player.getDataCollected());
        assertEquals(1, this.gamesLost);
        board.detach(this);
    }

    @Override
    public void onLose() {
        this.gamesLost++;
    }
}

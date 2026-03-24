package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.BoardObserver;
import ca.sfu.cmpt276.group15.board.TestHelper;
import ca.sfu.cmpt276.group15.board.tile.Floor;
import ca.sfu.cmpt276.group15.board.tile.TileType;
import ca.sfu.cmpt276.group15.math.Direction;
import ca.sfu.cmpt276.group15.math.Position;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FirewallTest extends CollectableTest implements BoardObserver{
    int gamesLost = 0;

    @Override
    protected Collectable createCollectable(Board board, int x, int y) {
        return new Firewall(board, x, y);
    }

    /**
     * Test that player can die of collecting firewall if it has no score.
     */
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

    /**
     * Test off-point for player dying of firewall.
     */
    @Test
    void damagePlayerWithScore() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Collectable collectable = this.createCollectable(board, 2, 2);
        Player player = new Player(board, 1, 2);
        player.adjustData(-collectable.value);

        board.attach(this);
        board.addEntity(collectable);
        board.addEntity(player);

        player.move(Direction.RIGHT);

        assertTrue(collectable.isRemoved());
        assertEquals(0, player.getDataCollected());
        assertEquals(0, this.gamesLost);
        board.detach(this);
    }

    /**
     * Test on-point for player dying of firewall.
     */
    @Test
    void damagePlayerWithNotEnoughScore() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Collectable collectable = this.createCollectable(board, 2, 2);
        Player player = new Player(board, 1, 2);
        player.adjustData(-collectable.value - 1);

        board.attach(this);
        board.addEntity(collectable);
        board.addEntity(player);

        player.move(Direction.RIGHT);

        assertTrue(collectable.isRemoved());
        assertEquals(-1, player.getDataCollected());
        assertEquals(1, this.gamesLost);
        board.detach(this);
    }

    /**
     * Ensure that firewalls can spread with ample space around them.
     */
    @Test
    void firewallSpread() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Firewall firewall = new Firewall(board, 2, 2);
        board.addEntity(firewall);

        boolean spread = false;
        for (int i = 0; i < 10000 && !spread; i++) {
            board.tick();
            for (Direction direction : Direction.values()) {
                Position pos = firewall.getPosition().relative(direction);
                Collection<Entity> entities = board.getEntitiesAt(pos.x(), pos.y());
                for (Entity entity : entities) {
                    if (entity instanceof Firewall) {
                        spread = true;
                        break;
                    }
                }
            }
        }

        assertTrue(spread, "firewall did not spread within 10000 ticks!");
    }

    /**
     * Ensure that firewalls cannot spread into walls.
     */
    @Test
    void spreadBlockedByWall() {
        Board board = TestHelper.createEnclosedBoard(3, 3);
        Firewall firewall = new Firewall(board, 1, 1);
        board.addEntity(firewall);

        boolean spread = false;
        for (int i = 0; i < 10000 && !spread; i++) {
            board.tick();
            if (board.iterateEntitiesYield(entity -> entity instanceof Firewall
                && !entity.getPosition().equals(firewall.getPosition()) ? true : null) != null) {
                spread = true;
            }
        }

        assertFalse(spread, "firewall spread into solid tiles!");
    }

    /**
     * Ensure that firewalls cannot spread into other entities.
     */
    @Test
    void spreadBlockedByData() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Firewall firewall = new Firewall(board, 2, 2);
        board.addEntity(firewall);
        for (Direction value : Direction.values()) {
            Position pos = firewall.getPosition().relative(value);
            board.addEntity(new Data(board, pos.x(), pos.y()));
        }

        boolean spread = false;
        for (int i = 0; i < 10000 && !spread; i++) {
            board.tick();
            if (board.iterateEntitiesYield(entity -> entity instanceof Firewall
                && !entity.getPosition().equals(firewall.getPosition()) ? true : null) != null) {
                spread = true;
            }
        }

        assertFalse(spread, "firewall spread into tile containing data!");
    }

    /**
     * Ensure that firewalls cannot spread off of the board.
     */
    @Test
    void cannotSpreadOOB() {
        Board board = new Board(new TileType[][] {{Floor.INSTANCE}});
        Firewall firewall = new Firewall(board, 0, 0);
        board.addEntity(firewall);

        boolean spread = false;
        for (int i = 0; i < 10000 && !spread; i++) {
            board.tick();
            if (board.iterateEntitiesYield(entity -> entity != firewall ? true : null) != null) {
                spread = true;
            }
        }

        assertFalse(spread, "firewall spread off of the board!");
    }

    /**
     * Ensure that firewalls cannot when the board is frozen.
     */
    @Test
    void cannotSpreadFrozen() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Firewall firewall = new Firewall(board, 2, 2);
        board.addEntity(firewall);
        board.freezeFirewallsFor(10000);

        boolean spread = false;
        for (int i = 1; i < 10000 && !spread; i++) {
            board.tick();
            for (Direction direction : Direction.values()) {
                Position pos = firewall.getPosition().relative(direction);
                Collection<Entity> entities = board.getEntitiesAt(pos.x(), pos.y());
                for (Entity entity : entities) {
                    if (entity instanceof Firewall) {
                        spread = true;
                        break;
                    }
                }
            }
        }

        assertFalse(spread, "firewall spread while the board was frozen!");
    }

    @Override
    public void onLose() {
        this.gamesLost++;
    }
}

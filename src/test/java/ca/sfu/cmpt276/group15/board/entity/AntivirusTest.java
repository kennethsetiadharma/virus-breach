package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.TestHelper;
import ca.sfu.cmpt276.group15.board.tile.Floor;
import ca.sfu.cmpt276.group15.board.tile.TileType;
import ca.sfu.cmpt276.group15.board.tile.Wall;
import ca.sfu.cmpt276.group15.math.Direction;
import ca.sfu.cmpt276.group15.math.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AntivirusTest {
    /**
     * Ensure that antivirus can navigate leftward.
     */
    @Test
    void navigateLeft() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Antivirus antivirus = new Antivirus(board, 2, 2);

        board.addEntity(antivirus);
        board.addEntity(new Player(board, 1, 2));

        for (int i = 0; i < Antivirus.MOVEMENT_TICKS; i++) board.tick();

        assertEquals(new Position(1, 2), antivirus.getPosition());
    }

    /**
     * Ensure that antivirus can navigate rightward.
     */
    @Test
    void navigateRight() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Antivirus antivirus = new Antivirus(board, 2, 2);

        board.addEntity(antivirus);
        board.addEntity(new Player(board, 3, 2));

        for (int i = 0; i < Antivirus.MOVEMENT_TICKS; i++) board.tick();

        assertEquals(new Position(3, 2), antivirus.getPosition());
    }

    /**
     * Ensure that antivirus can navigate upward.
     */
    @Test
    void navigateUp() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Antivirus antivirus = new Antivirus(board, 2, 2);

        board.addEntity(antivirus);
        board.addEntity(new Player(board, 2, 1));

        for (int i = 0; i < Antivirus.MOVEMENT_TICKS; i++) board.tick();

        assertEquals(new Position(2, 1), antivirus.getPosition());
    }

    /**
     * Ensure that antivirus can navigate downward.
     */
    @Test
    void navigateDown() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Antivirus antivirus = new Antivirus(board, 2, 2);

        board.addEntity(antivirus);
        board.addEntity(new Player(board, 2, 3));

        for (int i = 0; i < Antivirus.MOVEMENT_TICKS; i++) board.tick();

        assertEquals(new Position(2, 3), antivirus.getPosition());
    }

    /**
     * Ensure that antivirus can navigate multiple steps.
     */
    @Test
    void navigateDiagonal() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Antivirus antivirus = new Antivirus(board, 2, 2);

        board.addEntity(antivirus);
        board.addEntity(new Player(board, 1, 1));

        for (int i = 0; i < Antivirus.MOVEMENT_TICKS * 2; i++) board.tick();

        assertEquals(new Position(1, 1), antivirus.getPosition());
    }

    /**
     * Ensure that antivirus can navigate around an obstacle.
     */
    @Test
    void navigateAround() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Antivirus antivirus = new Antivirus(board, 3, 2);

        board.addEntity(antivirus);
        board.setTile(2, 2, Wall.INSTANCE);
        board.addEntity(new Player(board, 1, 2));

        for (int i = 0; i < Antivirus.MOVEMENT_TICKS * 4; i++) {
            board.tick();
            assertNotEquals(new Position(2, 2), antivirus.getPosition());
        }

        assertEquals(new Position(1, 2), antivirus.getPosition());
    }

    /**
     * Antivirus is inactive when player is unreachable.
     */
    @Test
    void noPathIdle() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Antivirus antivirus = new Antivirus(board, 3, 3);

        board.addEntity(antivirus);
        board.setTile(1, 1, Floor.INSTANCE);
        board.setTile(1, 2, Wall.INSTANCE);
        board.setTile(2, 2, Wall.INSTANCE);
        board.setTile(2, 1, Wall.INSTANCE);
        board.addEntity(new Player(board, 1, 1));

        // should not move
        for (int i = 0; i < Antivirus.MOVEMENT_TICKS * 10; i++) {
            board.tick();
            assertEquals(new Position(3, 3), antivirus.getPosition());
        }
        board.removeEntity(antivirus);
    }


    /**
     * Antivirus will not pathfind off of the board.
     */
    @Test
    void noPathOOB() {
        Board board = new Board(new TileType[][]{{Floor.INSTANCE, Wall.INSTANCE, Floor.INSTANCE}});
        Antivirus antivirus = new Antivirus(board, 0, 0);

        board.addEntity(antivirus);
        board.addEntity(new Player(board, 2, 0));

        // should not move
        for (int i = 0; i < Antivirus.MOVEMENT_TICKS * 10; i++) {
            board.tick();
            assertEquals(new Position(0, 0), antivirus.getPosition());
        }
        board.removeEntity(antivirus);
    }

    /**
     * Antivirus is inactive when there is no player.
     */
    @Test
    void noPlayerIdle() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Antivirus antivirus = new Antivirus(board, 2, 2);

        board.addEntity(antivirus);

        // should not move
        for (int i = 0; i < Antivirus.MOVEMENT_TICKS * 10; i++) {
            board.tick();
            assertEquals(new Position(2, 2), antivirus.getPosition());
        }
        board.removeEntity(antivirus);
    }

    /**
     * Antivirus is inactive when it has already at the same point as the player.
     */
    @Test
    void noPathCaught() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Antivirus antivirus = new Antivirus(board, 2, 2);

        board.addEntity(antivirus);
        board.addEntity(new Player(board, 2, 2));

        // should not move
        for (int i = 0; i < Antivirus.MOVEMENT_TICKS; i++) board.tick();

        assertEquals(new Position(2, 2), antivirus.getPosition());
        board.removeEntity(antivirus);
    }

    /**
     * Pathfind to player when even if it requires going further away temporarily.
     */
    @Test
    void navigateFar() {
        Board board = TestHelper.createEnclosedBoard(10, 10);

        Player player = new Player(board, 1, 1);
        Antivirus antivirus = new Antivirus(board, 8, 1);

        //WWWWWWWWWW
        //WPW     AW
        //W WWWWWW W
        //W W    W W
        //W W    W W
        //W W    W W
        //W W    W W
        //W WWWWWW W
        //W        W
        //WWWWWWWWWW
        for (int i = 2; i < 8; i++) {
            board.setTile(i, 2, Wall.INSTANCE);
            board.setTile(i, 7, Wall.INSTANCE);
            board.setTile(2, i, Wall.INSTANCE);
            board.setTile(7, i, Wall.INSTANCE);
        }
        board.setTile(2, 1, Wall.INSTANCE);

        board.addEntity(antivirus);
        board.addEntity(player);

        for (int i = 0; i < Antivirus.MOVEMENT_TICKS; i++) board.tick();

        assertEquals(new Position(8, 1).relative(Direction.DOWN), antivirus.getPosition());
        board.removeEntity(antivirus);
    }

    /**
     * Pathfind around a large obstacle.
     */
    @Test
    void pathfindAroundLarge() {
        Board board = TestHelper.createEnclosedBoard(10, 10);

        Player player = new Player(board, 1, 1);
        Antivirus antivirus = new Antivirus(board, 8, 8);

        //WWWWWWWWWW
        //WP       W
        //W WWWWWW W
        //W W      W
        //W W      W
        //W W      W
        //W W      W
        //W W      W
        //W       AW
        //WWWWWWWWWW
        for (int i = 2; i < 8; i++) {
            board.setTile(i, 2, Wall.INSTANCE);
            board.setTile(2, i, Wall.INSTANCE);
        }

        board.addEntity(antivirus);
        board.addEntity(player);

        for (int i = 0; i < Antivirus.MOVEMENT_TICKS; i++) board.tick();

        // ensure antivirus found player
        assertNotEquals(new Position(8, 1), antivirus.getPosition());
        board.removeEntity(antivirus);
    }
}

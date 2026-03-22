package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.tile.Floor;
import ca.sfu.cmpt276.group15.board.tile.TileType;
import ca.sfu.cmpt276.group15.board.tile.Wall;
import ca.sfu.cmpt276.group15.math.Position;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AntivirusTest {
    /**
     * Ensure that antivirus can navigate leftward.
     */
    @Test
    void navigateLeft() {
        Board board = createEnclosedBoard();
        Antivirus antivirus = new Antivirus(board, 2, 2);

        board.addEntity(antivirus);
        board.addEntity(new Player(board, 1, 2));
        board.tick();

        for (int i = 0; i < Antivirus.MOVEMENT_TICKS; i++) board.tick();

        assertEquals(new Position(1, 2), antivirus.getPosition());
    }

    /**
     * Ensure that antivirus can navigate rightward.
     */
    @Test
    void navigateRight() {
        Board board = createEnclosedBoard();
        Antivirus antivirus = new Antivirus(board, 2, 2);

        board.addEntity(antivirus);
        board.addEntity(new Player(board, 3, 2));
        board.tick();

        for (int i = 0; i < Antivirus.MOVEMENT_TICKS; i++) board.tick();

        assertEquals(new Position(3, 2), antivirus.getPosition());
    }

    /**
     * Ensure that antivirus can navigate upward.
     */
    @Test
    void navigateUp() {
        Board board = createEnclosedBoard();
        Antivirus antivirus = new Antivirus(board, 2, 2);

        board.addEntity(antivirus);
        board.addEntity(new Player(board, 2, 1));
        board.tick();

        for (int i = 0; i < Antivirus.MOVEMENT_TICKS; i++) board.tick();

        assertEquals(new Position(2, 1), antivirus.getPosition());
    }

    /**
     * Ensure that antivirus can navigate downward.
     */
    @Test
    void navigateDown() {
        Board board = createEnclosedBoard();
        Antivirus antivirus = new Antivirus(board, 2, 2);

        board.addEntity(antivirus);
        board.addEntity(new Player(board, 2, 3));
        board.tick();

        for (int i = 0; i < Antivirus.MOVEMENT_TICKS; i++) board.tick();

        assertEquals(new Position(2, 3), antivirus.getPosition());
    }

    /**
     * Ensure that antivirus can navigate multiple steps.
     */
    @Test
    void navigateDiagonal() {
        Board board = createEnclosedBoard();
        Antivirus antivirus = new Antivirus(board, 2, 2);

        board.addEntity(antivirus);
        board.addEntity(new Player(board, 1, 1));
        board.tick();

        for (int i = 0; i < Antivirus.MOVEMENT_TICKS * 2; i++) board.tick();

        assertEquals(new Position(1, 1), antivirus.getPosition());
    }

    /**
     * Ensure that antivirus can navigate around an obstacle.
     */
    @Test
    void navigateAround() {
        Board board = createEnclosedBoard();
        Antivirus antivirus = new Antivirus(board, 3, 2);

        board.addEntity(antivirus);
        board.setTile(2, 2, Wall.INSTANCE);
        board.addEntity(new Player(board, 1, 2));
        board.tick();

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
        Board board = createEnclosedBoard();
        Antivirus antivirus = new Antivirus(board, 3, 3);

        board.addEntity(antivirus);
        board.setTile(1, 1, Floor.INSTANCE);
        board.setTile(1, 2, Wall.INSTANCE);
        board.setTile(2, 2, Wall.INSTANCE);
        board.setTile(2, 1, Wall.INSTANCE);
        board.addEntity(new Player(board, 1, 1));
        board.tick();

        // should not move
        for (int i = 0; i < Antivirus.MOVEMENT_TICKS * 10; i++) {
            board.tick();
            assertEquals(new Position(3, 3), antivirus.getPosition());
        }
        board.removeEntity(antivirus);
    }

    /**
     * Antivirus is inactive when there is no player.
     */
    @Test
    void noPlayerIdle() {
        Board board = createEnclosedBoard();
        Antivirus antivirus = new Antivirus(board, 2, 2);

        board.addEntity(antivirus);
        board.tick();

        // should not move
        for (int i = 0; i < Antivirus.MOVEMENT_TICKS * 10; i++) {
            board.tick();
            assertEquals(new Position(2, 2), antivirus.getPosition());
        }
        board.removeEntity(antivirus);
    }

    private static Board createEnclosedBoard() {
        TileType[][] tiles = new TileType[5][5];
        for (int i = 0; i < 5; i++) {
            if (i != 0 && i != 4) Arrays.fill(tiles[i], Floor.INSTANCE);

            tiles[0][i] = Wall.INSTANCE;
            tiles[4][i] = Wall.INSTANCE;
            tiles[i][0] = Wall.INSTANCE;
            tiles[i][4] = Wall.INSTANCE;
        }
        return new Board(tiles);
    }
}

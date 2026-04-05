package ca.sfu.cmpt276.virusbreach.board.entity;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.board.TestHelper;
import ca.sfu.cmpt276.virusbreach.board.tile.TileTypes;
import ca.sfu.cmpt276.virusbreach.math.Direction;
import ca.sfu.cmpt276.virusbreach.math.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecryptionKeyTest {
    /**
     * Ensure a door is unlocked when a player collects a decryption key.
     */
    @Test
    void unlockDoor() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Position doorPos = new Position(0, 2);
        DecryptionKey key = new DecryptionKey(board, new Position(2, 2), doorPos);
        Player player = new Player(board, new Position(3, 2));
        board.setTile(doorPos, TileTypes.WALL);
        board.addEntity(key);
        board.addEntity(player);

        player.move(Direction.LEFT);

        assertTrue(key.isRemoved());
        assertEquals(TileTypes.OPEN_DOOR, board.getTile(doorPos));
    }
    /**
     * Ensure that the antivirus will not unlock the door on contact.
     */
    @Test
    void noAntivirusInteraction() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Position doorPos = new Position(0, 2);
        DecryptionKey key = new DecryptionKey(board, new Position(2, 2), doorPos);
        Antivirus antivirus = new Antivirus(board, new Position(3, 2));
        board.setTile(doorPos, TileTypes.LOCKED_DOOR);
        board.addEntity(key);
        board.addEntity(antivirus);

        antivirus.move(Direction.LEFT);

        assertFalse(key.isRemoved());
        assertEquals(TileTypes.LOCKED_DOOR, board.getTile(doorPos));
    }

}

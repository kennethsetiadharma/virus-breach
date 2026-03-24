package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.TestHelper;
import ca.sfu.cmpt276.group15.board.tile.LockedDoor;
import ca.sfu.cmpt276.group15.board.tile.OpenDoor;
import ca.sfu.cmpt276.group15.math.Direction;
import ca.sfu.cmpt276.group15.math.Position;
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
        DecryptionKey key = new DecryptionKey(board, 2, 2, doorPos);
        Player player = new Player(board, 3, 2);
        board.setTile(doorPos.x(), doorPos.y(), LockedDoor.INSTANCE);
        board.addEntity(key);
        board.addEntity(player);

        player.move(Direction.LEFT);

        assertTrue(key.isRemoved());
        assertEquals(OpenDoor.INSTANCE, board.getTile(doorPos));
    }
    /**
     * Ensure that the antivirus will not unlock the door on contact.
     */
    @Test
    void noAntivirusInteraction() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Position doorPos = new Position(0, 2);
        DecryptionKey key = new DecryptionKey(board, 2, 2, doorPos);
        Antivirus antivirus = new Antivirus(board, 3, 2);
        board.setTile(doorPos.x(), doorPos.y(), LockedDoor.INSTANCE);
        board.addEntity(key);
        board.addEntity(antivirus);

        antivirus.move(Direction.LEFT);

        assertFalse(key.isRemoved());
        assertEquals(LockedDoor.INSTANCE, board.getTile(doorPos));
    }

}

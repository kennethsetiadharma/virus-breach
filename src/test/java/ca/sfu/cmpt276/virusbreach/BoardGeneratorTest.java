package ca.sfu.cmpt276.virusbreach;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.board.TestHelper;
import ca.sfu.cmpt276.virusbreach.board.tile.Floor;
import ca.sfu.cmpt276.virusbreach.board.tile.Wall;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardGeneratorTest {

    // Board large enough to contain both rooms:
    // Server room: width=16, height=12 (top-left)
    // Storage room: STORAGE_FROM_RIGHT=11, STORAGE_FROM_BOTTOM=8 (bottom-right)
    // Use 46x26 to match actual game dimensions
    private static final int W = 46;
    private static final int H = 26;

    // Derived storage room constants (mirrors BoardGenerator logic)
    private static final int STORAGE_LEFT_X = W - 1 - 11; // 34
    private static final int STORAGE_TOP_Y  = H - 1 - 8;  // 17
    private static final int STORAGE_DOOR_Y = STORAGE_TOP_Y + 8 / 2; // 21

    /** Calling the constructor should not throw. */
    @Test
    void constructorInstantiates() {
        assertDoesNotThrow(BoardGenerator::new);
    }

    /** Wall at x=0 (left perimeter) should not be placed. */
    @Test
    void placeWall_leftBoundary_notPlaced() {
        Board board = TestHelper.createEnclosedBoard(W, H);
        board.setTile(1, 5, Floor.INSTANCE);
        BoardGenerator.placeWall(board, 0, 5);
        assertFalse(board.getTile(1, 5).isSolid());
    }

    /** Wall inside the server room interior should not be placed. */
    @Test
    void placeWall_insideServerRoom_notPlaced() {
        Board board = TestHelper.createEnclosedBoard(W, H);
        int x = 5; // inside: 0 < x < 16
        int y = 5; // inside: 0 < y < 12
        board.setTile(x, y, Floor.INSTANCE);
        BoardGenerator.placeWall(board, x, y);
        assertFalse(board.getTile(x, y).isSolid());
    }

    /** Wall on the server room doorway tile should not be placed. */
    @Test
    void placeWall_serverRoomDoorway_notPlaced() {
        Board board = TestHelper.createEnclosedBoard(W, H);
        int doorX = 16;     // SERVER_ROOM_WIDTH
        int doorY = 12 / 2; // SERVER_ROOM_HEIGHT / 2 = 6
        board.setTile(doorX, doorY, Floor.INSTANCE);
        BoardGenerator.placeWall(board, doorX, doorY);
        assertFalse(board.getTile(doorX, doorY).isSolid());
    }

    /** Wall in the 1-tile buffer right of the server room should not be placed. */
    @Test
    void placeWall_serverRoomRightBuffer_notPlaced() {
        Board board = TestHelper.createEnclosedBoard(W, H);
        int x = 16 + 1; // SERVER_ROOM_WIDTH + 1 = 17
        int y = 6;       // within 1..SERVER_ROOM_HEIGHT
        board.setTile(x, y, Floor.INSTANCE);
        BoardGenerator.placeWall(board, x, y);
        assertFalse(board.getTile(x, y).isSolid());
    }

    /** Wall in the 1-tile buffer below the server room should not be placed. */
    @Test
    void placeWall_serverRoomBottomBuffer_notPlaced() {
        Board board = TestHelper.createEnclosedBoard(W, H);
        int x = 6;      // within 1..SERVER_ROOM_WIDTH
        int y = 12 + 1; // SERVER_ROOM_HEIGHT + 1 = 13
        board.setTile(x, y, Floor.INSTANCE);
        BoardGenerator.placeWall(board, x, y);
        assertFalse(board.getTile(x, y).isSolid());
    }

    /** Wall inside the storage room interior should not be placed. */
    @Test
    void placeWall_insideStorageRoom_notPlaced() {
        Board board = TestHelper.createEnclosedBoard(W, H);
        int x = STORAGE_LEFT_X + 2; // inside: x > storageLeftX && x < W-1
        int y = STORAGE_TOP_Y + 2;  // inside: y > storageTopY && y < H-1
        board.setTile(x, y, Floor.INSTANCE);
        BoardGenerator.placeWall(board, x, y);
        assertFalse(board.getTile(x, y).isSolid());
    }

    /** Wall on the storage room doorway tile should not be placed. */
    @Test
    void placeWall_storageRoomDoorway_notPlaced() {
        Board board = TestHelper.createEnclosedBoard(W, H);
        board.setTile(STORAGE_LEFT_X, STORAGE_DOOR_Y, Floor.INSTANCE);
        BoardGenerator.placeWall(board, STORAGE_LEFT_X, STORAGE_DOOR_Y);
        assertFalse(board.getTile(STORAGE_LEFT_X, STORAGE_DOOR_Y).isSolid());
    }

    /** Wall in the 1-tile buffer left of the storage room should not be placed. */
    @Test
    void placeWall_storageRoomLeftBuffer_notPlaced() {
        Board board = TestHelper.createEnclosedBoard(W, H);
        int x = STORAGE_LEFT_X - 1;
        int y = STORAGE_TOP_Y + 1; // within storageTopY..H-2
        board.setTile(x, y, Floor.INSTANCE);
        BoardGenerator.placeWall(board, x, y);
        assertFalse(board.getTile(x, y).isSolid());
    }

    /** Wall in the 1-tile buffer above the storage room should not be placed. */
    @Test
    void placeWall_storageRoomTopBuffer_notPlaced() {
        Board board = TestHelper.createEnclosedBoard(W, H);
        int x = STORAGE_LEFT_X + 1; // within storageLeftX..W-2
        int y = STORAGE_TOP_Y - 1;
        board.setTile(x, y, Floor.INSTANCE);
        BoardGenerator.placeWall(board, x, y);
        assertFalse(board.getTile(x, y).isSolid());
    }

    /** Wall on an already-solid tile should be a no-op (tile stays wall). */
    @Test
    void placeWall_alreadySolid_noChange() {
        Board board = TestHelper.createEnclosedBoard(W, H);
        int x = 20;
        int y = 5;
        board.setTile(x, y, Wall.INSTANCE);
        BoardGenerator.placeWall(board, x, y);
        assertTrue(board.getTile(x, y).isSolid());
    }

    /** Wall on a valid open floor tile should be placed. */
    @Test
    void placeWall_validOpenTile_placed() {
        Board board = TestHelper.createEnclosedBoard(W, H);
        int x = 20;
        int y = 5;
        board.setTile(x, y, Floor.INSTANCE);
        BoardGenerator.placeWall(board, x, y);
        assertTrue(board.getTile(x, y).isSolid());
    }
}

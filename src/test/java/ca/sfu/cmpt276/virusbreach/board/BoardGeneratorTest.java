package ca.sfu.cmpt276.virusbreach.board;

import ca.sfu.cmpt276.virusbreach.board.entity.Antivirus;
import ca.sfu.cmpt276.virusbreach.board.entity.Data;
import ca.sfu.cmpt276.virusbreach.board.entity.Firewall;
import ca.sfu.cmpt276.virusbreach.board.entity.Player;
import ca.sfu.cmpt276.virusbreach.board.tile.TileType;
import ca.sfu.cmpt276.virusbreach.board.tile.TileTypes;
import ca.sfu.cmpt276.virusbreach.math.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardGeneratorTest {
    private static final int WIDTH  = 46;
    private static final int HEIGHT = 26;
    private static final int SERVER_ROOM_WIDTH  = 16;
    private static final int SERVER_ROOM_HEIGHT = 12;
    private static final int STORAGE_LEFT_X = WIDTH  - 1 - 11; // 34
    private static final int STORAGE_TOP_Y  = HEIGHT - 1 - 8;  // 17
    private static final int STORAGE_DOOR_Y = STORAGE_TOP_Y + 8 / 2; // 21

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(new TileType[HEIGHT][WIDTH]);
        BoardGenerator.generateBoard(board);
    }

    /**
     * Ensure that BoardGenerator cannot be constructed.
     */
    @Test
    void cannotConstruct() {
        assertThrows(UnsupportedOperationException.class, BoardGenerator::new);
    }

    /**
     * Board dimensions are set correctly after generation.
     */
    @Test
    void boardDimensions() {
        assertEquals(WIDTH,  board.width());
        assertEquals(HEIGHT, board.height());
    }

    /**
     * Every tile on the outer perimeter is a Wall, Entrance, or Exit.
     */
    @Test
    void outerPerimeterIsWallsOrSpecialTiles() {
        for (int x = 0; x < WIDTH; x++) {
            assertTrue(isPerimeterTile(board.getTile(new Position(x, 0))),          "top row at x=" + x);
            assertTrue(isPerimeterTile(board.getTile(new Position(x, HEIGHT - 1))), "bottom row at x=" + x);
        }
        for (int y = 0; y < HEIGHT; y++) {
            assertSame(TileTypes.WALL, board.getTile(new Position(0, y)),         "left wall at y=" + y);
            assertSame(TileTypes.WALL, board.getTile(new Position(WIDTH - 1, y)), "right wall at y=" + y);
        }
    }

    /**
     * The server room doorway stays as a LockedDoor, not overwritten by placeWall.
     */
    @Test
    void serverRoomDoorwayNotOverwritten() {
        assertEquals(TileTypes.LOCKED_DOOR, board.getTile(new Position(SERVER_ROOM_WIDTH, SERVER_ROOM_HEIGHT / 2)));
    }

    /**
     * The 1-tile buffer to the right of the server room is never walled.
     */
    @Test
    void serverRoomRightBufferNotWalled() {
        for (int y = 1; y <= SERVER_ROOM_HEIGHT; y++) {
            assertFalse(board.getTile(new Position(SERVER_ROOM_WIDTH + 1, y)).isSolid(),
                    "server room right buffer at y=" + y);
        }
    }

    /**
     * The 1-tile buffer below the server room is never walled.
     */
    @Test
    void serverRoomBottomBufferNotWalled() {
        for (int x = 1; x <= SERVER_ROOM_WIDTH; x++) {
            assertFalse(board.getTile(new Position(x, SERVER_ROOM_HEIGHT + 1)).isSolid(),
                    "server room bottom buffer at x=" + x);
        }
    }

    /**
     * The storage room doorway is not solid.
     */
    @Test
    void storageRoomDoorwayNotWalled() {
        assertFalse(board.getTile(new Position(STORAGE_LEFT_X, STORAGE_DOOR_Y)).isSolid());
    }

    /**
     * The 1-tile buffer to the left of the storage room is never walled.
     */
    @Test
    void storageRoomLeftBufferNotWalled() {
        for (int y = STORAGE_TOP_Y; y <= HEIGHT - 2; y++) {
            assertFalse(board.getTile(new Position(STORAGE_LEFT_X - 1, y)).isSolid(),
                    "storage room left buffer at y=" + y);
        }
    }

    /**
     * The 1-tile buffer above the storage room is never walled.
     */
    @Test
    void storageRoomTopBufferNotWalled() {
        for (int x = STORAGE_LEFT_X; x <= WIDTH - 2; x++) {
            assertFalse(board.getTile(new Position(x, STORAGE_TOP_Y - 1)).isSolid(),
                    "storage room top buffer at x=" + x);
        }
    }

    /**
     * Exactly 6 Data entities are spawned.
     */
    @Test
    void correctDataCount() {
        int[] count = {0};
        board.iterateEntities(e -> { if (e instanceof Data) count[0]++; });
        assertEquals(BoardGenerator.TOTAL_DATA, count[0]);
    }

    /**
     * Exactly 10 Firewall entities are spawned.
     */
    @Test
    void correctFirewallCount() {
        int[] count = {0};
        board.iterateEntities(e -> { if (e instanceof Firewall) count[0]++; });
        assertEquals(10, count[0]);
    }

    /**
     * Exactly 1 Antivirus entity is spawned.
     */
    @Test
    void correctAntivirusCount() {
        int[] count = {0};
        board.iterateEntities(e -> { if (e instanceof Antivirus) count[0]++; });
        assertEquals(1, count[0]);
    }

    /**
     * Exactly 1 Player entity is spawned.
     */
    @Test
    void correctPlayerCount() {
        int[] count = {0};
        board.iterateEntities(e -> { if (e instanceof Player) count[0]++; });
        assertEquals(1, count[0]);
    }

    /**
     * placeWall does nothing on a border tile.
     */
    @Test
    void placeWallSkipsBorder() {
        TileType before = board.getTile(new Position(0, 5));
        BoardGenerator.placeWall(board, new Position(0, 5));
        assertEquals(before, board.getTile(new Position(0, 5)));
    }

    /**
     * placeWall does nothing inside the server room interior.
     */
    @Test
    void placeWallSkipsServerRoomInterior() {
        TileType before = board.getTile(new Position(5, 5));
        BoardGenerator.placeWall(board, new Position(5, 5));
        assertEquals(before, board.getTile(new Position(5, 5)));
    }

    /**
     * placeWall does nothing on the server room doorway.
     */
    @Test
    void placeWallSkipsServerRoomDoorway() {
        TileType before = board.getTile(new Position(SERVER_ROOM_WIDTH, SERVER_ROOM_HEIGHT / 2));
        BoardGenerator.placeWall(board, new Position(SERVER_ROOM_WIDTH, SERVER_ROOM_HEIGHT / 2));
        assertEquals(before, board.getTile(new Position(SERVER_ROOM_WIDTH, SERVER_ROOM_HEIGHT / 2)));
    }

    /**
     * placeWall does nothing in the right buffer column of the server room.
     */
    @Test
    void placeWallSkipsServerRoomRightBuffer() {
        TileType before = board.getTile(new Position(SERVER_ROOM_WIDTH + 1, 5));
        BoardGenerator.placeWall(board, new Position(SERVER_ROOM_WIDTH + 1, 5));
        assertEquals(before, board.getTile(new Position(SERVER_ROOM_WIDTH + 1, 5)));
    }

    /**
     * placeWall does nothing in the bottom buffer row of the server room.
     */
    @Test
    void placeWallSkipsServerRoomBottomBuffer() {
        TileType before = board.getTile(new Position(5, SERVER_ROOM_HEIGHT + 1));
        BoardGenerator.placeWall(board, new Position(5, SERVER_ROOM_HEIGHT + 1));
        assertEquals(before, board.getTile(new Position(5, SERVER_ROOM_HEIGHT + 1)));
    }

    /**
     * placeWall does nothing on the storage room doorway.
     */
    @Test
    void placeWallSkipsStorageRoomDoorway() {
        TileType before = board.getTile(new Position(STORAGE_LEFT_X, STORAGE_DOOR_Y));
        BoardGenerator.placeWall(board, new Position(STORAGE_LEFT_X, STORAGE_DOOR_Y));
        assertEquals(before, board.getTile(new Position(STORAGE_LEFT_X, STORAGE_DOOR_Y)));
    }

    /**
     * placeWall does nothing in the left buffer column of the storage room.
     */
    @Test
    void placeWallSkipsStorageRoomLeftBuffer() {
        TileType before = board.getTile(new Position(STORAGE_LEFT_X - 1, STORAGE_TOP_Y + 1));
        BoardGenerator.placeWall(board, new Position(STORAGE_LEFT_X - 1, STORAGE_TOP_Y + 1));
        assertEquals(before, board.getTile(new Position(STORAGE_LEFT_X - 1, STORAGE_TOP_Y + 1)));
    }

    /**
     * placeWall does nothing in the top buffer row of the storage room.
     */
    @Test
    void placeWallSkipsStorageRoomTopBuffer() {
        TileType before = board.getTile(new Position(STORAGE_LEFT_X + 1, STORAGE_TOP_Y - 1));
        BoardGenerator.placeWall(board, new Position(STORAGE_LEFT_X + 1, STORAGE_TOP_Y - 1));
        assertEquals(before, board.getTile(new Position(STORAGE_LEFT_X + 1, STORAGE_TOP_Y - 1)));
    }

    /**
     * placeWall places a wall on a free floor tile.
     */
    @Test
    void placeWallOnFreeTile() {
        board.setTile(new Position(20, 5), TileTypes.FLOOR);
        BoardGenerator.placeWall(board, new Position(20, 5));
        assertSame(TileTypes.WALL, board.getTile(new Position(20, 5)));
    }

    /**
     * placeWall does not overwrite an already solid tile.
     */
    @Test
    void placeWallDoesNotOverwriteSolid() {
        board.setTile(new Position(20, 5), TileTypes.WALL);
        BoardGenerator.placeWall(board, new Position(20, 5));
        assertEquals(TileTypes.WALL, board.getTile(new Position(20, 5)));
    }

    /**
     * x matches the server room right buffer column but y is outside the buffer range —
     * line 201 evaluates but falls through to subsequent checks.
     */
    @Test
    void placeWallServerRoomRightBufferYOutOfRange() {
        int x = SERVER_ROOM_WIDTH + 1;
        int y = SERVER_ROOM_HEIGHT + 1; // y <= SERVER_ROOM_HEIGHT is false, falls through
        board.setTile(new Position(x, y), TileTypes.FLOOR);
        BoardGenerator.placeWall(board, new Position(x, y));
        assertSame(TileTypes.WALL, board.getTile(new Position(x, y)));
    }

    /**
     * y matches the server room bottom buffer row but x is outside the buffer range —
     * line 203 evaluates but falls through to subsequent checks.
     */
    @Test
    void placeWallServerRoomBottomBufferXOutOfRange() {
        int x = SERVER_ROOM_WIDTH + 2;
        int y = SERVER_ROOM_HEIGHT + 1; // x <= SERVER_ROOM_WIDTH is false, falls through
        board.setTile(new Position(x, y), TileTypes.FLOOR);
        BoardGenerator.placeWall(board, new Position(x, y));
        assertSame(TileTypes.WALL, board.getTile(new Position(x, y)));
    }

    /**
     * x matches the storage room left buffer column but y is above the buffer range —
     * line 213 evaluates but falls through to subsequent checks.
     */
    @Test
    void placeWallStorageRoomLeftBufferYOutOfRange() {
        int x = STORAGE_LEFT_X - 1;
        int y = STORAGE_TOP_Y - 1; // y >= STORAGE_TOP_Y is false, falls through
        board.setTile(new Position(x, y), TileTypes.FLOOR);
        BoardGenerator.placeWall(board, new Position(x, y));
        assertSame(TileTypes.WALL, board.getTile(new Position(x, y)));
    }

    /**
     * y matches the storage room top buffer row but x is left of the buffer range —
     * line 215 evaluates but falls through to subsequent checks.
     */
    @Test
    void placeWallStorageRoomTopBufferXOutOfRange() {
        int x = STORAGE_LEFT_X - 1;
        int y = STORAGE_TOP_Y - 1; // x >= STORAGE_LEFT_X is false, falls through
        board.setTile(new Position(x, y), TileTypes.FLOOR);
        BoardGenerator.placeWall(board, new Position(x, y));
        assertSame(TileTypes.WALL, board.getTile(new Position(x, y)));
    }

    /**
     * x in server room x-range but y is at the server room bottom boundary —
     * line 197 evaluates partially but falls through (y < SERVER_ROOM_HEIGHT is false).
     */
    @Test
    void placeWallServerRoomInteriorFalseBranch() {
        int x = 5;
        int y = SERVER_ROOM_HEIGHT; // y < SERVER_ROOM_HEIGHT is false
        TileType before = board.getTile(new Position(x, y));
        BoardGenerator.placeWall(board, new Position(x, y));
        assertEquals(before, board.getTile(new Position(x, y)));
    }

    /**
     * placeWall does nothing when x is on the right border.
     */
    @Test
    void placeWallSkipsBorderRight() {
        TileType before = board.getTile(new Position(WIDTH - 1, 5));
        BoardGenerator.placeWall(board, new Position(WIDTH - 1, 5));
        assertEquals(before, board.getTile(new Position(WIDTH - 1, 5)));
    }

    /**
     * placeWall does nothing when y is on the top border.
     */
    @Test
    void placeWallSkipsBorderTop() {
        TileType before = board.getTile(new Position(5, 0));
        BoardGenerator.placeWall(board, new Position(5, 0));
        assertEquals(before, board.getTile(new Position(5, 0)));
    }

    /**
     * placeWall does nothing when y is on the bottom border.
     */
    @Test
    void placeWallSkipsBorderBottom() {
        TileType before = board.getTile(new Position(5, HEIGHT - 1));
        BoardGenerator.placeWall(board, new Position(5, HEIGHT - 1));
        assertEquals(before, board.getTile(new Position(5, HEIGHT - 1)));
    }

    private boolean isPerimeterTile(TileType tile) {
        return tile == TileTypes.WALL || tile == TileTypes.ENTRANCE || tile == TileTypes.EXIT;
    }
}

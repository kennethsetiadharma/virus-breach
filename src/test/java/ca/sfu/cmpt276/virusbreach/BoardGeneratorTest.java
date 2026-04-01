package ca.sfu.cmpt276.virusbreach;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.board.entity.Antivirus;
import ca.sfu.cmpt276.virusbreach.board.entity.Data;
import ca.sfu.cmpt276.virusbreach.board.entity.Firewall;
import ca.sfu.cmpt276.virusbreach.board.entity.Player;
import ca.sfu.cmpt276.virusbreach.board.tile.Entrance;
import ca.sfu.cmpt276.virusbreach.board.tile.Exit;
import ca.sfu.cmpt276.virusbreach.board.tile.LockedDoor;
import ca.sfu.cmpt276.virusbreach.board.tile.TileType;
import ca.sfu.cmpt276.virusbreach.board.tile.Wall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardGeneratorTest {
    // board dimensions matching VirusBreach constants
    private static final int WIDTH  = 46;
    private static final int HEIGHT = 26;

    // mirrors BoardGenerator's private constants
    private static final int SERVER_ROOM_WIDTH  = 16;
    private static final int SERVER_ROOM_HEIGHT = 12;
    private static final int STORAGE_LEFT_X     = WIDTH  - 1 - 11; // 34
    private static final int STORAGE_TOP_Y      = HEIGHT - 1 - 8;  // 17
    private static final int STORAGE_DOOR_Y     = STORAGE_TOP_Y + 8 / 2; // 21

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(new TileType[HEIGHT][WIDTH]);
        BoardGenerator.generateBoard(board);
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
     * Every tile on the outer perimeter is a Wall, Entrance, or Exit — nothing else.
     */
    @Test
    void outerPerimeterIsWallsOrSpecialTiles() {
        for (int x = 0; x < WIDTH; x++) {
            assertTrue(isPerimeterTile(board.getTile(x, 0)),          "top row at x=" + x);
            assertTrue(isPerimeterTile(board.getTile(x, HEIGHT - 1)), "bottom row at x=" + x);
        }
        for (int y = 0; y < HEIGHT; y++) {
            assertInstanceOf(Wall.class, board.getTile(0, y),         "left wall at y=" + y);
            assertInstanceOf(Wall.class, board.getTile(WIDTH - 1, y), "right wall at y=" + y);
        }
    }

    private boolean isPerimeterTile(TileType tile) {
        return tile instanceof Wall || tile instanceof Entrance || tile instanceof Exit;
    }

    /**
     * placeWall skips the server room doorway — it stays LockedDoor, not Wall.
     */
    @Test
    void serverRoomDoorwayNotOverwritten() {
        int doorY = SERVER_ROOM_HEIGHT / 2;
        assertEquals(LockedDoor.INSTANCE, board.getTile(SERVER_ROOM_WIDTH, doorY));
    }

    /**
     * placeWall keeps every non-doorway tile in the server room right wall solid.
     */
    @Test
    void serverRoomRightWallIsSolid() {
        int doorY = SERVER_ROOM_HEIGHT / 2;
        for (int y = 1; y <= SERVER_ROOM_HEIGHT; y++) {
            if (y != doorY) {
                assertTrue(board.getTile(SERVER_ROOM_WIDTH, y).isSolid(),
                        "server room right wall at y=" + y + " should be solid");
            }
        }
    }

    /**
     * placeWall skips the 1-tile buffer to the right of the server room right wall.
     */
    @Test
    void serverRoomRightBufferNotWalled() {
        for (int y = 1; y <= SERVER_ROOM_HEIGHT; y++) {
            assertFalse(board.getTile(SERVER_ROOM_WIDTH + 1, y).isSolid(),
                    "server room right buffer at y=" + y + " should not be a wall");
        }
    }

    /**
     * placeWall skips the 1-tile buffer below the server room bottom wall.
     */
    @Test
    void serverRoomBottomBufferNotWalled() {
        for (int x = 1; x <= SERVER_ROOM_WIDTH; x++) {
            assertFalse(board.getTile(x, SERVER_ROOM_HEIGHT + 1).isSolid(),
                    "server room bottom buffer at x=" + x + " should not be a wall");
        }
    }

    /**
     * placeWall skips the storage room doorway — that tile must not be solid.
     */
    @Test
    void storageRoomDoorwayNotWalled() {
        assertFalse(board.getTile(STORAGE_LEFT_X, STORAGE_DOOR_Y).isSolid());
    }

    /**
     * placeWall skips the 1-tile buffer to the left of the storage room left wall.
     */
    @Test
    void storageRoomLeftBufferNotWalled() {
        for (int y = STORAGE_TOP_Y; y <= HEIGHT - 2; y++) {
            assertFalse(board.getTile(STORAGE_LEFT_X - 1, y).isSolid(),
                    "storage room left buffer at y=" + y + " should not be a wall");
        }
    }

    /**
     * placeWall skips the 1-tile buffer above the storage room top wall.
     */
    @Test
    void storageRoomTopBufferNotWalled() {
        for (int x = STORAGE_LEFT_X; x <= WIDTH - 2; x++) {
            assertFalse(board.getTile(x, STORAGE_TOP_Y - 1).isSolid(),
                    "storage room top buffer at x=" + x + " should not be a wall");
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
}

package ca.sfu.cmpt276.group15;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.ServerRoom;
import ca.sfu.cmpt276.group15.board.StorageRoom;
import ca.sfu.cmpt276.group15.board.entity.*;
import ca.sfu.cmpt276.group15.board.tile.Entrance;
import ca.sfu.cmpt276.group15.board.tile.Exit;
import ca.sfu.cmpt276.group15.board.tile.Floor;
import ca.sfu.cmpt276.group15.board.tile.LockedDoor;
import ca.sfu.cmpt276.group15.board.tile.Wall;
import ca.sfu.cmpt276.group15.math.Position;

/**
 * Generates the initial state of the game board.
 * Responsible for placing tiles, carving out special rooms,
 * scattering random wall shapes, and spawning all entities.
 */
public class BoardGenerator {
    // Server room dimensions — carved out of the top-left corner of the board.
    private static final int SERVER_ROOM_WIDTH  = 16;
    private static final int SERVER_ROOM_HEIGHT = 12;

    // Storage room dimensions — carved out of the bottom-right corner of the board.
    // Left wall is 11 tiles from the right outer wall; top wall is 6 tiles up from the bottom outer wall.
    private static final int STORAGE_FROM_RIGHT  = 11; // left wall at x = width - 12
    private static final int STORAGE_FROM_BOTTOM =  8; // top wall at  y = height - 7

    /**
     * How many normal (required) rewards to spawn on the board
     */
    public static final int TOTAL_DATA = 6;

    /**
     * Generates all tiles, rooms, and entities on the given board.
     * This includes the outer perimeter, server and storage rooms,
     * random wall shapes, and all initial entity spawns.
     *
     * @param board the board to generate
     */
    public static void generateBoard(Board board) {
        int width  = board.width();
        int height = board.height();

        // Fill entire board with floor
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                board.setTile(x, y, Floor.INSTANCE);
            }
        }

        // Outer perimeter walls
        for (int x = 1; x < width - 1; x++) {
            board.setTile(x, 0, Wall.INSTANCE);
            board.setTile(x, height - 1, Wall.INSTANCE);
        }
        for (int y = 1; y < height - 1; y++) {
            board.setTile(0, y, Wall.INSTANCE);
            board.setTile(width - 1, y, Wall.INSTANCE);
        }

        // Corner walls
        board.setTile(0, 0, Wall.INSTANCE);
        board.setTile(0, height - 1, Wall.INSTANCE);
        board.setTile(width - 1, height - 1, Wall.INSTANCE);
        board.setTile(width - 1, 0, Wall.INSTANCE);

        // Server room — top-left corner, bounded by the outer perimeter on the top and left.
        // Right boundary wall: vertical at x = SERVER_ROOM_WIDTH, with a doorway at mid-height.
        int doorY = SERVER_ROOM_HEIGHT / 2;
        for (int y = 1; y <= SERVER_ROOM_HEIGHT; y++) {
            board.setTile(SERVER_ROOM_WIDTH, y, y == doorY ? LockedDoor.INSTANCE : Wall.INSTANCE);
        }
        // Bottom boundary wall: horizontal at y = SERVER_ROOM_HEIGHT.
        for (int x = 1; x <= SERVER_ROOM_WIDTH; x++) {
            board.setTile(x, SERVER_ROOM_HEIGHT, Wall.INSTANCE);
        }

        // Storage room — bottom-right corner, bounded by the outer perimeter on the right and bottom.
        // Walls are placed before entity spawning so entities cannot occupy wall positions.
        int storageLeftX = width  - 1 - STORAGE_FROM_RIGHT;  // x = width-12
        int storageTopY  = height - 1 - STORAGE_FROM_BOTTOM; // y = height-9
        int storageDoorY = storageTopY + STORAGE_FROM_BOTTOM / 2; // mid-height of the left wall

        // Left wall
        for (int y = storageTopY; y <= height - 2; y++) {
            if (y == storageDoorY) continue; // doorway gap
            board.setTile(storageLeftX, y, Wall.INSTANCE);
        }
        // Top wall
        for (int x = storageLeftX; x <= width - 2; x++) {
            board.setTile(x, storageTopY, Wall.INSTANCE);
        }

        // Player entrance: random position along the bottom perimeter, excluding the storage room's x range
        int playerX = board.getRandom().nextInt(1, storageLeftX);
        board.setTile(playerX, height - 1, Entrance.INSTANCE);
        board.addEntity(new Player(board, playerX, height - 1));

        // Exit: top perimeter, outside the server room's top edge
        board.setTile(board.getRandom().nextInt(SERVER_ROOM_WIDTH + 1, width - 1), 0, Exit.INSTANCE);

        // Furnish rooms before entity spawning so their internal walls exist when spawnAnywhere runs
        ServerRoom serverRoom = new ServerRoom(0, 0, SERVER_ROOM_WIDTH, SERVER_ROOM_HEIGHT,
                new Position(SERVER_ROOM_WIDTH, doorY));
        serverRoom.furnishRoom(board);

        StorageRoom storageRoom = new StorageRoom(storageLeftX, storageTopY,
                STORAGE_FROM_RIGHT, STORAGE_FROM_BOTTOM, new Position(storageLeftX, storageDoorY),
                new Position(SERVER_ROOM_WIDTH, doorY));
        storageRoom.furnishRoom(board);

        // Internal maze walls across the whole board, skipping the server room interior
        generateWalls(board, width, height);

        // Spawn entities across the whole board interior
        for (int i = 0; i < 6; i++) {
            spawnAnywhere(board, 1, 1, width - 1, height - 1, Data::new);
        }
        for (int i = 0; i < 10; i++) {
            spawnAnywhere(board, 1, 1, width - 1, height - 1, Firewall::new);
        }
        spawnAnywhere(board, 1, 1, width - 1, height - 1, Antivirus::new);
    }

    /**
     * Repeatedly picks a random tile within the given bounds until a free,
     * non-solid, empty tile is found, then spawns the entity there.
     *
     * @param board    the board to spawn on
     * @param minX     minimum x-coordinate (inclusive)
     * @param minY     minimum y-coordinate (inclusive)
     * @param maxX     maximum x-coordinate (exclusive)
     * @param maxY     maximum y-coordinate (exclusive)
     * @param supplier factory that creates the entity given the board and coordinates
     */
    public static void spawnAnywhere(Board board, int minX, int minY, int maxX, int maxY, EntitySupplier supplier) {
        while (true) {
            int x = board.getRandom().nextInt(minX, maxX);
            int y = board.getRandom().nextInt(minY, maxY);

            if (!board.getTile(x, y).isSolid() && board.getEntitiesAt(x, y).isEmpty()) {
                board.addEntity(supplier.create(board, x, y));
                break;
            }
        }
    }

    /**
     * Scatters random wall shapes across the board interior.
     * Each shape is one of: a single tile, a 2-tile path (horizontal or vertical),
     * or a 2×2 square.
     */
    private static void generateWalls(Board board, int width, int height) {
        int numShapes = 45;

        for (int i = 0; i < numShapes; i++) {
            int x = board.getRandom().nextInt(2, width - 3);
            int y = board.getRandom().nextInt(2, height - 3);

            switch (board.getRandom().nextInt(3)) {
                case 0 -> // single tile
                    placeWall(board, x, y);
                case 1 -> { // 2-tile path 
                    if (board.getRandom().nextBoolean()) {
                        placeWall(board, x,     y);
                        placeWall(board, x + 1, y);
                    } else {
                        placeWall(board, x, y);
                        placeWall(board, x, y + 1);
                    }
                }
                case 2 -> { // 2×2 square
                    placeWall(board, x,     y);
                    placeWall(board, x + 1, y);
                    placeWall(board, x,     y + 1);
                    placeWall(board, x + 1, y + 1);
                }
            }
        }
    }

    /**
     * Places a wall at (x, y) if the tile is inside the playable area,
     * not inside a special room, not within 1 tile of a room boundary wall,
     * not a room doorway, and not already solid.
     */
    private static void placeWall(Board board, int x, int y) {
        int width  = board.width();
        int height = board.height();

        if (x <= 0 || x >= width - 1 || y <= 0 || y >= height - 1) return;

        // Skip server room interior and its doorway
        if (x > 0 && x < SERVER_ROOM_WIDTH && y > 0 && y < SERVER_ROOM_HEIGHT) return;
        if (x == SERVER_ROOM_WIDTH && y == SERVER_ROOM_HEIGHT / 2) return;

        // 1-tile buffer outside the server room's right wall (x = SERVER_ROOM_WIDTH)
        if (x == SERVER_ROOM_WIDTH + 1 && y >= 1 && y <= SERVER_ROOM_HEIGHT) return;
        // 1-tile buffer outside the server room's bottom wall (y = SERVER_ROOM_HEIGHT)
        if (y == SERVER_ROOM_HEIGHT + 1 && x >= 1 && x <= SERVER_ROOM_WIDTH) return;

        // Skip storage room interior and its doorway
        int storageLeftX = width  - 1 - STORAGE_FROM_RIGHT;
        int storageTopY  = height - 1 - STORAGE_FROM_BOTTOM;
        int storageDoorY = storageTopY + STORAGE_FROM_BOTTOM / 2;
        if (x > storageLeftX && x < width - 1 && y > storageTopY && y < height - 1) return;
        if (x == storageLeftX && y == storageDoorY) return;

        // 1-tile buffer outside the storage room's left wall (x = storageLeftX)
        if (x == storageLeftX - 1 && y >= storageTopY && y <= height - 2) return;
        // 1-tile buffer outside the storage room's top wall (y = storageTopY)
        if (y == storageTopY - 1 && x >= storageLeftX && x <= width - 2) return;

        if (!board.getTile(x, y).isSolid())
            board.setTile(x, y, Wall.INSTANCE);
    }

    @FunctionalInterface
    public interface EntitySupplier {
        Entity create(Board board, int x, int y);
    }
}

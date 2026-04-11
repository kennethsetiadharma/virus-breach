package ca.sfu.cmpt276.virusbreach.board;

import ca.sfu.cmpt276.virusbreach.board.entity.*;
import ca.sfu.cmpt276.virusbreach.board.tile.TileTypes;
import ca.sfu.cmpt276.virusbreach.math.Position;

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
     * Number of firewall entities to spawn on the board
     */
    private static final int TOTAL_FIREWALLS = 10;

    /**
     * Number of antivirus entities to spawn on the board
     */
    private static final int TOTAL_ANTIVIRUS = 1;

    /**
     * Number of wall shapes to randomly scatter across the board
     */
    private static final int WALL_SHAPES_COUNT = 45;

    BoardGenerator() {
        throw new UnsupportedOperationException("Class cannot be constructed");
    }

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

        // Fill the board and place outer walls
        placeOuterWalls(board, width, height);

        // Create server and storage rooms with their walls and doors
        placeRooms(board, width, height);

        // Add random maze walls throughout the interior
        generateWalls(board, width, height);

        // Spawn all entities (data, firewalls, antivirus)
        spawnAllEntities(board, width, height);
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

            if (!board.getTile(new Position(x,y)).isSolid() && board.getEntitiesAt(new Position(x, y)).isEmpty()) {
                board.addEntity(supplier.create(board, new Position(x, y)));
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
        for (int i = 0; i < WALL_SHAPES_COUNT; i++) {
            int x = board.getRandom().nextInt(2, width - 3);
            int y = board.getRandom().nextInt(2, height - 3);

            switch (board.getRandom().nextInt(3)) {
                case 0 -> // single tile
                    placeWall(board, new Position(x, y));
                case 1 -> { // 2-tile path
                    if (board.getRandom().nextBoolean()) {
                        placeWall(board, new Position(x,     y));
                        placeWall(board, new Position(x + 1, y));
                    } else {
                        placeWall(board, new Position(x, y));
                        placeWall(board, new Position(x, y + 1));
                    }
                }
                default -> { // 2×2 square
                    placeWall(board, new Position(x,     y));
                    placeWall(board, new Position(x + 1, y));
                    placeWall(board, new Position(x,     y + 1));
                    placeWall(board, new Position(x + 1, y + 1));
                }
            }
        }
    }

    /**
     * Places a wall at the given position if it's a valid location.
     * A valid location is one that's within bounds, not in restricted areas,
     * and not already solid.
     */
    static void placeWall(Board board, Position position) {
        if (!isValidWallPosition(board, position)) return;

        if (!board.getTile(position).isSolid()) {
            board.setTile(position, TileTypes.WALL);
        }
    }

    /**
     * Checks if the given position is valid for wall placement.
     * A position is valid if it's within bounds and not in any restricted area.
     */
    private static boolean isValidWallPosition(Board board, Position position) {
        int width = board.width();
        int height = board.height();

        return isWithinBounds(position, width, height) &&
               !isInServerRoomArea(position) &&
               !isInStorageRoomArea(position, width, height) &&
               !isInBufferZone(position, width, height);
    }

    /**
     * Checks if the position is within the playable bounds (not on the outer perimeter).
     */
    private static boolean isWithinBounds(Position position, int width, int height) {
        return position.x() > 0 && position.x() < width - 1 &&
               position.y() > 0 && position.y() < height - 1;
    }

    /**
     * Checks if the position is inside the server room interior or at its doorway.
     */
    private static boolean isInServerRoomArea(Position position) {
        // Server room interior
        if (position.x() > 0 && position.x() < SERVER_ROOM_WIDTH &&
            position.y() > 0 && position.y() < SERVER_ROOM_HEIGHT) {
            return true;
        }

        // Server room doorway
        return position.x() == SERVER_ROOM_WIDTH && position.y() == SERVER_ROOM_HEIGHT / 2;
    }

    /**
     * Checks if the position is inside the storage room interior or at its doorway.
     */
    private static boolean isInStorageRoomArea(Position position, int width, int height) {
        int storageLeftX = width - 1 - STORAGE_FROM_RIGHT;
        int storageTopY = height - 1 - STORAGE_FROM_BOTTOM;
        int storageDoorY = storageTopY + STORAGE_FROM_BOTTOM / 2;

        // Storage room interior
        if (position.x() > storageLeftX && position.x() < width - 1 &&
            position.y() > storageTopY && position.y() < height - 1) {
            return true;
        }

        // Storage room doorway
        return position.x() == storageLeftX && position.y() == storageDoorY;
    }

    /**
     * Checks if the position is in a buffer zone (1-tile spacing around room walls).
     */
    private static boolean isInBufferZone(Position position, int width, int height) {
        return isInServerRoomBufferZone(position) || isInStorageRoomBufferZone(position, width, height);
    }

    /**
     * Checks if the position is in the 1-tile buffer zone around the server room.
     */
    private static boolean isInServerRoomBufferZone(Position position) {
        // Buffer outside server room's right wall
        if (position.x() == SERVER_ROOM_WIDTH + 1 &&
            position.y() >= 1 && position.y() <= SERVER_ROOM_HEIGHT) {
            return true;
        }

        // Buffer outside server room's bottom wall
        return position.y() == SERVER_ROOM_HEIGHT + 1 &&
               position.x() >= 1 && position.x() <= SERVER_ROOM_WIDTH;
    }

    /**
     * Checks if the position is in the 1-tile buffer zone around the storage room.
     */
    private static boolean isInStorageRoomBufferZone(Position position, int width, int height) {
        int storageLeftX = width - 1 - STORAGE_FROM_RIGHT;
        int storageTopY = height - 1 - STORAGE_FROM_BOTTOM;

        // Buffer outside storage room's left wall
        if (position.x() == storageLeftX - 1 &&
            position.y() >= storageTopY && position.y() <= height - 2) {
            return true;
        }

        // Buffer outside storage room's top wall
        return position.y() == storageTopY - 1 &&
               position.x() >= storageLeftX && position.x() <= width - 2;
    }

    /**
     * Fills the entire board with floor tiles and places outer perimeter walls.
     */
    private static void placeOuterWalls(Board board, int width, int height) {
        // Fill entire board with floor
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                board.setTile(new Position(x, y), TileTypes.FLOOR);
            }
        }

        // Outer perimeter walls
        for (int x = 1; x < width - 1; x++) {
            board.setTile(new Position(x, 0), TileTypes.WALL);
            board.setTile(new Position(x, height - 1), TileTypes.WALL);
        }
        for (int y = 1; y < height - 1; y++) {
            board.setTile(new Position(0, y), TileTypes.WALL);
            board.setTile(new Position(width - 1, y), TileTypes.WALL);
        }

        // Corner walls
        board.setTile(new Position(0, 0), TileTypes.WALL);
        board.setTile(new Position(0, height - 1), TileTypes.WALL);
        board.setTile(new Position(width - 1, height - 1), TileTypes.WALL);
        board.setTile(new Position(width - 1, 0), TileTypes.WALL);
    }

    /**
     * Creates server room and storage room with their walls, doors, and furnishings.
     * Also places player entrance and exit.
     */
    private static void placeRooms(Board board, int width, int height) {
        // Server room — top-left corner, bounded by the outer perimeter on the top and left.
        // Right boundary wall: vertical at x = SERVER_ROOM_WIDTH, with a doorway at mid-height.
        int doorY = SERVER_ROOM_HEIGHT / 2;
        for (int y = 1; y <= SERVER_ROOM_HEIGHT; y++) {
            board.setTile(new Position(SERVER_ROOM_WIDTH, y), y == doorY ? TileTypes.LOCKED_DOOR : TileTypes.WALL);
        }
        // Bottom boundary wall: horizontal at y = SERVER_ROOM_HEIGHT.
        for (int x = 1; x <= SERVER_ROOM_WIDTH; x++) {
            board.setTile(new Position(x, SERVER_ROOM_HEIGHT), TileTypes.WALL);
        }

        // Storage room — bottom-right corner, bounded by the outer perimeter on the right and bottom.
        // Walls are placed before entity spawning so entities cannot occupy wall positions.
        int storageLeftX = width  - 1 - STORAGE_FROM_RIGHT;  // x = width-12
        int storageTopY  = height - 1 - STORAGE_FROM_BOTTOM; // y = height-9
        int storageDoorY = storageTopY + STORAGE_FROM_BOTTOM / 2; // mid-height of the left wall

        // Left wall
        for (int y = storageTopY; y <= height - 2; y++) {
            if (y == storageDoorY) continue; // doorway gap
            board.setTile(new Position(storageLeftX, y), TileTypes.WALL);
        }
        // Top wall
        for (int x = storageLeftX; x <= width - 2; x++) {
            board.setTile(new Position(x, storageTopY), TileTypes.WALL);
        }

        // Player entrance: random position along the bottom perimeter, excluding the storage room's x range
        int playerX = board.getRandom().nextInt(1, storageLeftX);
        board.setTile(new Position(playerX, height - 1), TileTypes.ENTRANCE);
        board.addEntity(new Player(board, new Position(playerX, height - 1)));

        // Exit: top perimeter, outside the server room's top edge
        board.setTile(new Position(board.getRandom().nextInt(SERVER_ROOM_WIDTH + 1, width - 1), 0), TileTypes.EXIT);

        // Furnish rooms before entity spawning so their internal walls exist when spawnAnywhere runs
        // This prevents entities from spawning inside the rooms' internal wall layouts, which would trap
        // them and make them inaccessible to the player.
        ServerRoom serverRoom = new ServerRoom(new Position(0, 0), SERVER_ROOM_WIDTH, SERVER_ROOM_HEIGHT,
                new Position(SERVER_ROOM_WIDTH, doorY));
        serverRoom.furnishRoom(board);

        StorageRoom storageRoom = new StorageRoom(new Position(storageLeftX, storageTopY),
                STORAGE_FROM_RIGHT, STORAGE_FROM_BOTTOM, new Position(storageLeftX, storageDoorY),
                new Position(SERVER_ROOM_WIDTH, doorY));
        storageRoom.furnishRoom(board);
    }

    /**
     * Spawns all game entities (data, firewalls, antivirus) randomly across the board.
     */
    private static void spawnAllEntities(Board board, int width, int height) {
        // Spawn entities across the whole board interior
        for (int i = 0; i < TOTAL_DATA; i++) {
            spawnAnywhere(board, 1, 1, width - 1, height - 1, Data::new);
        }
        for (int i = 0; i < TOTAL_FIREWALLS; i++) {
            spawnAnywhere(board, 1, 1, width - 1, height - 1, Firewall::new);
        }
        for (int i = 0; i < TOTAL_ANTIVIRUS; i++) {
            spawnAnywhere(board, 1, 1, width - 1, height - 1, Antivirus::new);
        }
    }

    /**
     * Utility interface (for ease of using ::new) to construct an entity.
     */
    @FunctionalInterface
    public interface EntitySupplier {
        /**
         * Constructs a new entity on the board at the given position.
         *
         * @param board the board to spawn on
         * @param position the location to spawn at
         * @return the created entity
         */
        Entity create(Board board, Position position);
    }
}

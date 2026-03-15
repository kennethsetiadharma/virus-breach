package ca.sfu.cmpt276.group15;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.ServerRoom;
import ca.sfu.cmpt276.group15.board.StorageRoom;
import ca.sfu.cmpt276.group15.board.entity.*;
import ca.sfu.cmpt276.group15.board.tile.Entrance;
import ca.sfu.cmpt276.group15.board.tile.Exit;
import ca.sfu.cmpt276.group15.board.tile.Floor;
import ca.sfu.cmpt276.group15.board.tile.Wall;
import ca.sfu.cmpt276.group15.math.Position;

public class BoardGenerator {
    // Server room dimensions — carved out of the top-left corner of the board.
    private static final int SERVER_ROOM_WIDTH  = 16;
    private static final int SERVER_ROOM_HEIGHT = 12;

    // Storage room dimensions — carved out of the bottom-right corner of the board.
    // Left wall is 11 tiles from the right outer wall; top wall is 6 tiles up from the bottom outer wall.
    private static final int STORAGE_FROM_RIGHT  = 11; // left wall at x = width - 12
    private static final int STORAGE_FROM_BOTTOM =  8; // top wall at  y = height - 7

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
            if (y == doorY) continue; // leave gap for the doorway
            board.setTile(SERVER_ROOM_WIDTH, y, Wall.INSTANCE);
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

        // Furnish the server room (currently empty)
        ServerRoom serverRoom = new ServerRoom(0, 0, SERVER_ROOM_WIDTH, SERVER_ROOM_HEIGHT,
                new Position(SERVER_ROOM_WIDTH, doorY));
        serverRoom.furnishRoom(board);

        StorageRoom storageRoom = new StorageRoom(storageLeftX, storageTopY,
                STORAGE_FROM_RIGHT, STORAGE_FROM_BOTTOM, new Position(storageLeftX, storageDoorY));
        storageRoom.furnishRoom(board);
    }

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
     * Generates internal maze walls across the whole board, skipping the server room interior.
     */
    private static void generateWalls(Board board, int width, int height) {
        int roomWidth  = 12;
        int roomHeight = 12;

        for (int rx = 0; rx < width / roomWidth; rx++) {
            for (int ry = 0; ry < height / roomHeight; ry++) {
                int roomX = rx * roomWidth + 2;
                int roomY = ry * roomHeight + 2;

                // Skip sub-rooms that fall entirely inside the server room interior (top-left)
                if (roomX + roomWidth <= SERVER_ROOM_WIDTH && roomY + roomHeight <= SERVER_ROOM_HEIGHT) continue;

                // Skip sub-rooms that fall entirely inside the storage room interior (bottom-right)
                if (roomX >= width - 1 - STORAGE_FROM_RIGHT && roomY >= height - 1 - STORAGE_FROM_BOTTOM) continue;

                // Top and bottom walls of this sub-room
                for (int x = 0; x < roomWidth - 4; x++) {
                    if (x > 2 && x < roomWidth - 6) {
                        board.setTile(roomX + x, roomY, Wall.INSTANCE);
                        board.setTile(roomX + x, roomY + roomHeight - 4, Wall.INSTANCE);
                    }
                }

                // Left and right walls of this sub-room
                for (int y = 0; y < roomHeight - 4; y++) {
                    if (y > 2 && y < roomHeight - 6) {
                        board.setTile(roomX, roomY + y, Wall.INSTANCE);
                        board.setTile(roomX + roomWidth - 4, roomY + y, Wall.INSTANCE);
                    }
                }
            }
        }
    }

    @FunctionalInterface
    public static interface EntitySupplier {
        Entity create(Board board, int x, int y);
    }
}

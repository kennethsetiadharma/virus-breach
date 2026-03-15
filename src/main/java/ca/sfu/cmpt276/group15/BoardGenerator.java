package ca.sfu.cmpt276.group15;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.ServerRoom;
import ca.sfu.cmpt276.group15.board.entity.*;
import ca.sfu.cmpt276.group15.board.tile.Entrance;
import ca.sfu.cmpt276.group15.board.tile.Exit;
import ca.sfu.cmpt276.group15.board.tile.Floor;
import ca.sfu.cmpt276.group15.board.tile.Wall;
import ca.sfu.cmpt276.group15.math.Position;

public class BoardGenerator {
    // Width (in tiles) reserved for the server room on the left side of the board.
    // The dividing wall sits at x = SERVER_ROOM_WIDTH; the main room starts at x = SERVER_ROOM_WIDTH + 1.
    private static final int SERVER_ROOM_WIDTH = 20;

    public static void generateBoard(Board board) {
        int width = board.width();
        int height = board.height();
        int mainX = SERVER_ROOM_WIDTH + 1; // first x column of the main room interior

        // Fill entire board with floor
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                board.setTile(x, y, Floor.INSTANCE);
            }
        }

        // Outer perimeter walls (top, bottom, left, right edges)
        for (int x = 1; x < width - 1; x++) {
            board.setTile(x, 0, Wall.INSTANCE);
            board.setTile(x, height - 1, Wall.INSTANCE);
        }
        for (int y = 1; y < height - 1; y++) {
            board.setTile(0, y, Wall.INSTANCE);
            board.setTile(width - 1, y, Wall.INSTANCE);
        }

        // Dividing wall between the server room (left) and main room (right),
        // with a single-tile doorway at mid-height.
        int doorY = height / 2;
        for (int y = 1; y < height - 1; y++) {
            if (y == doorY) continue; // leave gap for the doorway
            board.setTile(SERVER_ROOM_WIDTH, y, Wall.INSTANCE);
        }

        // Player entrance and spawn — placed on the bottom edge of the main room
        int playerX = board.getRandom().nextInt(mainX, width - 1);
        board.setTile(playerX, height - 1, Entrance.INSTANCE);
        board.addEntity(new Player(board, playerX, height - 1));

        // Exit — placed on the top edge of the main room
        board.setTile(board.getRandom().nextInt(mainX, width - 1), 0, Exit.INSTANCE);

        // Internal maze walls, restricted to the main room section
        generateWalls(board, mainX, width, height);

        // Ensure all four corners remain solid walls
        board.setTile(0, 0, Wall.INSTANCE);
        board.setTile(0, height - 1, Wall.INSTANCE);
        board.setTile(width - 1, height - 1, Wall.INSTANCE);
        board.setTile(width - 1, 0, Wall.INSTANCE);

        // Spawn entities only in the main room
        for (int i = 0; i < 6; i++) {
            spawnAnywhere(board, mainX, 1, width - 1, height - 1, Data::new);
        }
        for (int i = 0; i < 10; i++) {
            spawnAnywhere(board, mainX, 1, width - 1, height - 1, Firewall::new);
        }
        spawnAnywhere(board, mainX, 1, width - 1, height - 1, Antivirus::new);

        // Create and furnish the server room (currently empty)
        ServerRoom serverRoom = new ServerRoom(0, 0, SERVER_ROOM_WIDTH, height, new Position(SERVER_ROOM_WIDTH, doorY));
        serverRoom.furnishRoom(board);
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
     * Generates internal maze walls within the main room section of the board.
     *
     * @param startX first x column belonging to the main room interior
     * @param width  total board width
     * @param height total board height
     */
    private static void generateWalls(Board board, int startX, int width, int height) {
        int roomWidth = 12;
        int roomHeight = 12;

        int mainRoomWidth = width - startX;

        for (int rx = 0; rx < mainRoomWidth / roomWidth; rx++) {
            for (int ry = 0; ry < height / roomHeight; ry++) {
                int roomX = startX + rx * roomWidth + 2;
                int roomY = ry * roomHeight + 2;

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

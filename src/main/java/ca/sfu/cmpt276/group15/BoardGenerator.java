package ca.sfu.cmpt276.group15;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.entity.*;
import ca.sfu.cmpt276.group15.board.tile.Entrance;
import ca.sfu.cmpt276.group15.board.tile.Exit;
import ca.sfu.cmpt276.group15.board.tile.Floor;
import ca.sfu.cmpt276.group15.board.tile.Wall;

public class BoardGenerator {
    public static void generateBoard(Board board) {
        int width = board.getWidth();
        int height = board.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                board.setTile(x, y, Floor.INSTANCE);
            }
        }

        // outer walls
        for (int x = 1; x < width - 1; x++) {
            board.setTile(x, 0, Wall.INSTANCE);
            board.setTile(x, height - 1, Wall.INSTANCE);
        }
        for (int y = 1; y < height - 1; y++) {
            board.setTile(0, y, Wall.INSTANCE);
            board.setTile(width - 1, y, Wall.INSTANCE);
        }

        int x = board.getRandom().nextInt(1, width - 2);
        board.setTile(x, height-1, Entrance.INSTANCE);
        board.addEntity(new Player(board, x, height-1));
        board.setTile(board.getRandom().nextInt(1, width-2), 0, Exit.INSTANCE);

        generateWalls(board, width, height);

        board.setTile(0, 0, Wall.INSTANCE);
        board.setTile(0, height - 1, Wall.INSTANCE);
        board.setTile(width - 1, height - 1, Wall.INSTANCE);
        board.setTile(width - 1, 0, Wall.INSTANCE);

        for (int i = 0; i < 6; i++) {
            spawnAnywhere(board, 1, 1, width-1, height-1, Data::new);
        }
        for (int i = 0; i < 10; i++) {
            spawnAnywhere(board, 1, 1, width-1, height-1, Firewall::new);
        }

        spawnAnywhere(board, 1, 1, width-1, height-1, Antivirus::new);
    }

    public static void spawnAnywhere(Board board, int minX, int minY, int width, int height, EntitySupplier supplier) {
        while (true) {
            int x = board.getRandom().nextInt(minX, width);
            int y = board.getRandom().nextInt(minY, height);

            if (board.getEntitiesAt(x, y).isEmpty()) {
                board.addEntity(supplier.create(board, x, y));
                break;
            }
        }
    }
    
    /**
     * Generates a room-based maze layout
     */
    private static void generateWalls(Board board, int width, int height) {
        int roomWidth = 12;
        int roomHeight = 12;
        
        // Create walls
        for (int rx = 0; rx < width / roomWidth; rx++) {
            for (int ry = 0; ry < height / roomHeight; ry++) {
                int roomX = rx * roomWidth + 2;
                int roomY = ry * roomHeight + 2;
                
                // Create room walls 
                // Top and bottom walls
                for (int x = 0; x < roomWidth - 4; x++) {
                    if (x > 2 && x < roomWidth - 6) { 
//                        trySpawn(board, "wall", roomX + x, roomY);
//                        trySpawn(board, "wall", roomX + x, roomY + roomHeight - 4);
                    }
                }
                
                // Left and right walls
                for (int y = 0; y < roomHeight - 4; y++) {
                    if (y > 2 && y < roomHeight - 6) {
//                        trySpawn(board, "wall", roomX, roomY + y);
//                        trySpawn(board, "wall", roomX + roomWidth - 4, roomY + y);
                    }
                }
            }
        }
    }
}

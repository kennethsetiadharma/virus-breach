package ca.sfu.cmpt276.group15;

import ca.sfu.cmpt276.group15.entity.EntityType;
import ca.sfu.cmpt276.group15.math.Position;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;

public class BoardGenerator {
    public static void generateBoard(GameWorld world, int width, int height) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                world.spawn("floor", Position.fromGrid(x), Position.fromGrid(y));
            }
        }

        for (int x = 1; x < width - 1; x++) {
            world.spawn("wall", Position.fromGrid(x), Position.fromGrid(0));
            world.spawn("wall", Position.fromGrid(x), Position.fromGrid(height - 1));
        }
        for (int y = 1; y < height - 1; y++) {
            world.spawn("wall", Position.fromGrid(0), Position.fromGrid(y));
            world.spawn("wall", Position.fromGrid(height - 1), Position.fromGrid(y));
        }


        generateWalls(world, width, height);

        Entity wall = world.getRandom(EntityType.WALL).get();
        wall.removeFromWorld();
        world.spawn("entrance", wall.getPosition());

        wall = world.getRandom(EntityType.WALL).get();
        wall.removeFromWorld();
        world.spawn("exit", wall.getPosition());

        world.spawn("wall", Position.fromGrid(0), Position.fromGrid(0));
        world.spawn("wall", Position.fromGrid(0), Position.fromGrid(height - 1));
        world.spawn("wall", Position.fromGrid(width - 1), Position.fromGrid(height - 1));
        world.spawn("wall", Position.fromGrid(width - 1), Position.fromGrid(0));

        int spawned = 0;
        while (spawned < 6) {
            if (trySpawn(world, "data", FXGLMath.random(1, width - 2), FXGLMath.random(1, height - 2))) {
                spawned++;
            }
        }

        spawned = 0;
        while (spawned < 10) {
            if (trySpawn(world, "firewall", FXGLMath.random(1, width - 2), FXGLMath.random(1, height - 2))) {
                spawned++;
            }
        }
        while (!trySpawn(world, "antivirus", FXGLMath.random(1, width - 2), FXGLMath.random(1, height - 2))) {
        }
    }

    public static boolean trySpawn(GameWorld world, String entity, int x, int y) {
        if (world.getEntitiesAt(Position.fromGrid(x, y)).stream().anyMatch(e -> !e.isType(EntityType.FLOOR))) {
            return false;
        }

        world.spawn(entity, Position.fromGrid(x), Position.fromGrid(y));
        return true;
    }
    
    /**
     * Generates a room-based maze layout
     */
    private static void generateWalls(GameWorld world, int width, int height) {
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
                        trySpawn(world, "wall", roomX + x, roomY);
                        trySpawn(world, "wall", roomX + x, roomY + roomHeight - 4);
                    }
                }
                
                // Left and right walls
                for (int y = 0; y < roomHeight - 4; y++) {
                    if (y > 2 && y < roomHeight - 6) {
                        trySpawn(world, "wall", roomX, roomY + y);
                        trySpawn(world, "wall", roomX + roomWidth - 4, roomY + y);
                    }
                }
            }
        }
    }
}

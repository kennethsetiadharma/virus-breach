package ca.sfu.cmpt276.group15.board;

import ca.sfu.cmpt276.group15.BoardGenerator;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.GameWorld;
import javafx.geometry.Point2D;

public class ServerRoom extends Room {
    public ServerRoom(int x, int y, int width, int height, Point2D entrance) {
        super(x, y, width, height, entrance);
    }

    @Override
    public boolean furnishRoom(GameWorld world) {
        generateInternalLayout(world);
        spawnEntities(world);
        return true;
    }

    @Override
    protected void spawnEntities(GameWorld world) {
        // None for now
    }

    @Override
    protected void generateInternalLayout(GameWorld world) {
        // No internal walls for now
    }
}

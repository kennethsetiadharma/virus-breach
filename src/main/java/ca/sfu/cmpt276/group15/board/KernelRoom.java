package ca.sfu.cmpt276.group15.board;

import ca.sfu.cmpt276.group15.BoardGenerator;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.GameWorld;
import javafx.geometry.Point2D;

public class KernelRoom extends Room {
    public KernelRoom(int x, int y, int width, int height, Point2D entrance) {
        // For now, kernel room is just an empty room with no entrance
        // We can add special features later if we want
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

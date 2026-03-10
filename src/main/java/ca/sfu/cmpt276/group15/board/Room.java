package ca.sfu.cmpt276.group15.board;

import ca.sfu.cmpt276.group15.BoardGenerator;
import com.almasb.fxgl.entity.GameWorld;
import javafx.geometry.Point2D;

public abstract class Room {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Point2D entrance;
    
    public Room(int x, int y, int width, int height, Point2D entrance) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.entrance = entrance;
    }
    
    public abstract boolean furnishRoom(GameWorld world);
    
    protected abstract void spawnEntities(GameWorld world);
    
    protected abstract void generateInternalLayout(GameWorld world);
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}

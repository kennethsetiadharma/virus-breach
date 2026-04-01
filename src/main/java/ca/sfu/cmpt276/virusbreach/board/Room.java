package ca.sfu.cmpt276.virusbreach.board;

import ca.sfu.cmpt276.virusbreach.math.Position;

public abstract class Room {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Position entrance;
    
    public Room(int x, int y, int width, int height, Position entrance) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.entrance = entrance;
    }
    
    public abstract boolean furnishRoom(Board board);
    
    protected abstract void spawnEntities(Board board);
    
    protected abstract void generateInternalLayout(Board board);
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}

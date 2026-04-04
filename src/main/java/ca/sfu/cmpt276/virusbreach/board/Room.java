package ca.sfu.cmpt276.virusbreach.board;

import ca.sfu.cmpt276.virusbreach.math.Position;

/**
 * A distinct area to be generated on the board,
 * potentially containing unique resources.
 */
public abstract class Room {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Position entrance;
    
    public Room(Position position, int width, int height, Position entrance) {
        this.x = position.x();
        this.y = position.y();
        this.width = width;
        this.height = height;
        this.entrance = entrance;
    }
    
    public abstract boolean furnishRoom(Board board);
    
    protected abstract void spawnEntities(Board board);
    
    protected abstract void generateInternalLayout(Board board);
}

package ca.sfu.cmpt276.virusbreach.board;

import ca.sfu.cmpt276.virusbreach.math.Position;

/**
 * A distinct area to be generated on the board,
 * potentially containing unique resources.
 */
public abstract class Room {
    /**
     * The leftmost x-coordinate of the room.
     */
    protected int x;
    /**
     * The topmost y-coordinate of the room.
     */
    protected int y;
    /**
     * The width of the room, in tiles.
     */
    protected int width;
    /**
     * The height of the room, in tiles.
     */
    protected int height;
    /**
     * The location of the entrance to this room.
     */
    protected Position entrance;

    /**
     * Constructs a new room with the specified dimensions.
     *
     * @param position the room's top-left corner position
     * @param width the width of the room, in tiles
     * @param height the height of the room, in tiles
     * @param entrance location of the entrance
     */
    public Room(Position position, int width, int height, Position entrance) {
        this.x = position.x();
        this.y = position.y();
        this.width = width;
        this.height = height;
        this.entrance = entrance;
    }

    /**
     * Places appropriate tiles and entities inside the room, to decorate it.
     *
     * @param board the board to generate on
     * @return whether generation was successful
     */
    public abstract boolean furnishRoom(Board board);
}

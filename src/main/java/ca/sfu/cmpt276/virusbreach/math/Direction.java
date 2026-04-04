package ca.sfu.cmpt276.virusbreach.math;

/**
 * Four cardinal directions.
 * Note that negative y is upwards on the board.
 */
public enum Direction {
    /**
     * Negative-Y offset, vertically upwards on the screen.
     */
    UP(0, -1),
    /**
     * Positive-Y offset, vertically downwards on the screen.
     */
    DOWN(0, 1),
    /**
     * Negative-X offset, horizontally leftward on the screen.
     */
    LEFT(-1, 0),
    /**
     * Positive-X offset, horizontally rightward on the screen.
     */
    RIGHT(1, 0);

    /**
     * The x-coordinate offset needed to move 1 unit in this direction
     */
    private final int x;

    /**
     * The y-coordinate offset needed to move 1 unit in this direction
     */
    private final int y;

    /**
     * Creates a direction with the given offsets.
     *
     * @param x x-offset to use
     * @param y y-offset to use
     */
    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Determines the direction of movement, based on the offset vector provided.
     *
     * @param x x-coordinate of the vector
     * @param y x-coordinate of the vector
     * @return the direction of movement that the vector represents
     * @throws IllegalArgumentException if the vector does not move in exactly one of the cardinal directions
     */
    public static Direction fromVector(int x, int y) {
        if ((x == 0 && y == 0) || (x != 0 && y != 0)) throw new IllegalArgumentException("Invalid vector");
        if (y < 0) return Direction.UP;
        if (y > 0) return Direction.DOWN;
        if (x < 0) return Direction.LEFT;
        return Direction.RIGHT;
    }

    /**
     * {@return the x offset}
     */
    public int getX() {
        return x;
    }

    /**
     * {@return the y offset}
     */
    public int getY() {
        return y;
    }
}

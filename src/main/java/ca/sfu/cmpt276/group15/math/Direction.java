package ca.sfu.cmpt276.group15.math;

/**
 * Four cardinal directions.
 * Note that negative y is upwards on the board.
 */
public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    /**
     * The x-coordinate offset needed to move 1 unit in this direction
     */
    private final int x;

    /**
     * The y-coordinate offset needed to move 1 unit in this direction
     */
    private final int y;

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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

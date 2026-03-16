package ca.sfu.cmpt276.group15.math;

/**
 * Represents a location on a board.
 *
 * @param x the x-coordinate
 * @param y the y-coordinate
 */
public record Position(int x, int y) {
    /**
     * 1 board position unit, represents this many JavaFX position units.
     */
    public static final int UNIT_SIZE = 16;

    /**
     * Converts from board coordinate space to JavaFX coordinate space
     *
     * @param position the position on the board
     * @return the position's associated JavaFX coordinate space position
     */
    public static double fromGrid(int position) {
        return position * UNIT_SIZE;
    }

    /**
     * Calculates the position one unit away in the given direction.
     *
     * @param direction the direction to move in
     * @return the adjacent position in the given direction
     */
    public Position relative(Direction direction) {
        return new Position(this.x + direction.getX(), this.y + direction.getY());
    }

    /**
     * Calculates the manhattan (taxicab) distance from this position to the given position.
     *
     * @param b the second point to calculate the distance from
     * @return the sum of the absolute values of the difference of each coordinate
     * @see <a href="https://en.wikipedia.org/wiki/Taxicab_geometry">the Wikipedia article</a>
     */
    public int manhattanDistance(Position b) {
        return Math.abs(this.x - b.x) + Math.abs(this.y - b.y);
    }
}

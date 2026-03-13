package ca.sfu.cmpt276.group15.math;

import javafx.geometry.Point2D;

public record Position(int x, int y) {
    public static final int UNIT_SIZE = 16;

    public static int asGrid(double position) {
        return Math.floorDiv((int) position, UNIT_SIZE);
    }

    public static double fromGrid(int position) {
        return position * UNIT_SIZE;
    }

    public static Point2D fromGrid(int x, int y) {
        return new Point2D(fromGrid(x), fromGrid(y));
    }

    public Position relative(Direction direction) {
        return new Position(this.x + direction.getX(), this.y + direction.getY());
    }

    public int manhattanDistance(Position b) {
        return Math.abs(this.x - b.x) + Math.abs(this.y - b.y);
    }
}

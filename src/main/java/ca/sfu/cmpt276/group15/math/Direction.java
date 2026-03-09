package ca.sfu.cmpt276.group15.math;

import javafx.geometry.Point2D;

public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final int x;
    private final int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

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

    public Point2D asVector() {
        return new Point2D(x, y);
    }
}

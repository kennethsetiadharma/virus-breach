package ca.sfu.cmpt276.group15.math;

public class Position {
    public static final int UNIT_SIZE = 16;

    public static int asGrid(double position) {
        return Math.floorDiv((int) position, UNIT_SIZE);
    }
    public static double fromGrid(int position) {
        return position * UNIT_SIZE;
    }
}

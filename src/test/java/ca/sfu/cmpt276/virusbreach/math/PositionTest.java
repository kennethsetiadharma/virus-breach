package ca.sfu.cmpt276.virusbreach.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionTest {
    /**
     * Moving up decreases y by 1.
     */
    @Test
    void relativeUp() {
        Position p = new Position(3, 3);
        assertEquals(new Position(3, 2), p.relative(Direction.UP));
    }

    /**
     * Moving down increases y by 1.
     */
    @Test
    void relativeDown() {
        Position p = new Position(3, 3);
        assertEquals(new Position(3, 4), p.relative(Direction.DOWN));
    }

    /**
     * Moving left decreases x by 1.
     */
    @Test
    void relativeLeft() {
        Position p = new Position(3, 3);
        assertEquals(new Position(2, 3), p.relative(Direction.LEFT));
    }

    /**
     * Moving right increases x by 1.
     */
    @Test
    void relativeRight() {
        Position p = new Position(3, 3);
        assertEquals(new Position(4, 3), p.relative(Direction.RIGHT));
    }

    /**
     * Distance from a point to itself is zero.
     */
    @Test
    void manhattanDistanceSamePoint() {
        Position p = new Position(2, 5);
        assertEquals(0, p.manhattanDistance(p));
    }

    /**
     * Manhattan distance between two known points is |dx| + |dy|.
     */
    @Test
    void manhattanDistanceKnown() {
        Position a = new Position(1, 1);
        Position b = new Position(4, 5);
        assertEquals(7, a.manhattanDistance(b));
    }

    /**
     * Distance is the same regardless of which point it is measured from.
     */
    @Test
    void manhattanDistanceSymmetric() {
        Position a = new Position(0, 0);
        Position b = new Position(3, 4);
        assertEquals(a.manhattanDistance(b), b.manhattanDistance(a));
    }

    /**
     * fromGrid converts board coordinates to JavaFX pixel space (multiply by UNIT_SIZE).
     */
    @Test
    void fromGridScalesCorrectly() {
        assertEquals(0.0, Position.fromGrid(0));
        assertEquals(16.0, Position.fromGrid(1));
        assertEquals(48.0, Position.fromGrid(3));
    }
}

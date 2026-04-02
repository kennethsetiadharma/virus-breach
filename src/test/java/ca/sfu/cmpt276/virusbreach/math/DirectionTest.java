package ca.sfu.cmpt276.virusbreach.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {
    /**
     * Cannot obtain a direction from a zero vector.
     */
    @Test
    void zeroVector() {
        assertThrowsExactly(IllegalArgumentException.class, () -> Direction.fromVector(new Position(0, 0)));
    }

    /**
     * Cannot create a direction from a vector that moves in multiple cardinal directions
     */
    @Test
    void fromMultipleDirections() {
        for (Direction d1 : Direction.values()) {
            for (Direction d2 : Direction.values()) {
                if ((d1.getX() == 0) != (d2.getX() == 0)) {
                    assertThrowsExactly(IllegalArgumentException.class, () -> Direction.fromVector(new Position(d1.getX() + d2.getX(), d1.getY() + d2.getY())));
                }
            }
        }
    }
}

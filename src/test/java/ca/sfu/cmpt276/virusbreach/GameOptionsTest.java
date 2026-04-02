package ca.sfu.cmpt276.virusbreach;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameOptionsTest {
    /**
     * Default volume is 0.5.
     */
    @Test
    void defaultVolume() {
        GameOptions opts = new GameOptions();
        assertEquals(0.5, opts.getVolume());
    }

    /**
     * Volume can be set to a value within the valid range.
     */
    @Test
    void setVolumeMidRange() {
        GameOptions opts = new GameOptions();
        opts.setVolume(0.75);
        assertEquals(0.75, opts.getVolume());
    }

    /**
     * Volume below 0 is clamped to 0.
     */
    @Test
    void setVolumeClampedToZero() {
        GameOptions opts = new GameOptions();
        opts.setVolume(-1.0);
        assertEquals(0.0, opts.getVolume());
    }

    /**
     * Volume above 1 is clamped to 1.
     */
    @Test
    void setVolumeClampedToOne() {
        GameOptions opts = new GameOptions();
        opts.setVolume(2.0);
        assertEquals(1.0, opts.getVolume());
    }

    /**
     * Volume can be set exactly to 0.
     */
    @Test
    void setVolumeZero() {
        GameOptions opts = new GameOptions();
        opts.setVolume(0.0);
        assertEquals(0.0, opts.getVolume());
    }

    /**
     * Volume can be set exactly to 1.
     */
    @Test
    void setVolumeOne() {
        GameOptions opts = new GameOptions();
        opts.setVolume(1.0);
        assertEquals(1.0, opts.getVolume());
    }
}

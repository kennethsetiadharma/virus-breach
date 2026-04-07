package ca.sfu.cmpt276.virusbreach;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTimeTest {
    @Test
    void cannotConstruct() {
        assertThrows(UnsupportedOperationException.class, GameTime::new);
    }
}

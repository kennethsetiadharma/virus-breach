package ca.sfu.cmpt276.virusbreach;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @Test
    void cannotConstruct() {
        assertThrows(UnsupportedOperationException.class, Main::new);
    }
}

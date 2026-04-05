package ca.sfu.cmpt276.virusbreach.board.tile;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class TileTypesTest {
    @Test
    void cannotConstruct() {
        assertThrows(UnsupportedOperationException.class, TileTypes::new);
    }
}

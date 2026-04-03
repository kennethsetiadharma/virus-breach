package ca.sfu.cmpt276.virusbreach.ui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RenderNodeRegistryTest {
    @Test
    void cannotConstruct() {
        assertThrows(UnsupportedOperationException.class, RenderNodeRegistry::new);
    }
}

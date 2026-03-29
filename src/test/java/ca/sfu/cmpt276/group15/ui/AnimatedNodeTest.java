package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.math.Position;
import javafx.scene.Node;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(ApplicationExtension.class)
class AnimatedNodeTest {
    @Test
    void idleFrameWhenIdle() {
        AnimatedNode node = new AnimatedNode("player.png", 2, "player_moving1.png", "player_moving2.png");
        Node idle = node.getChildrenUnmodifiable().getFirst();

        node.animate(new Position(2, 2), new Position(2, 2), 5);

        assertSame(idle, node.getChildrenUnmodifiable().getFirst());
        assertEquals(1.0, node.getChildrenUnmodifiable().getFirst().getScaleX());
    }

    @Test
    void usesMovingFrameAndFlips() {
        AnimatedNode node = new AnimatedNode("player.png", 2, "player_moving1.png", "player_moving2.png");
        Node idle = node.getChildrenUnmodifiable().getFirst();

        node.animate(new Position(1, 2), new Position(2, 2), 2);

        Node current = node.getChildrenUnmodifiable().getFirst();
        assertNotSame(idle, current);
        assertEquals(-1.0, current.getScaleX());
    }

    @Test
    void idleFallbackNoMovingFrames() {
        AnimatedNode node = new AnimatedNode("player.png", 2);
        Node idle = node.getChildrenUnmodifiable().getFirst();

        node.animate(new Position(2, 2), new Position(1, 2), 4);

        assertSame(idle, node.getChildrenUnmodifiable().getFirst());
        assertEquals(1.0, node.getChildrenUnmodifiable().getFirst().getScaleX());
    }
}

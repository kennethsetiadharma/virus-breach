package ca.sfu.cmpt276.group15.ui;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.junit.jupiter.api.Test;

import static ca.sfu.cmpt276.group15.math.Position.UNIT_SIZE;
import static org.junit.jupiter.api.Assertions.*;

class ResourceManagerTest {
    @Test
    void spriteFallsBackToRectangle() {
        Node node = ResourceManager.sprite("fakeasset.png", Color.HOTPINK);

        assertInstanceOf(javafx.scene.shape.Rectangle.class, node);
    }

    @Test
    void createImageViewUsesUnitSize() {
        Image image = ResourceManager.loadIcon("pause.png");
        assertNotNull(image);

        ImageView view = ResourceManager.createImageView(image);

        assertEquals(UNIT_SIZE, view.getFitWidth());
        assertEquals(UNIT_SIZE, view.getFitHeight());
        assertFalse(view.isPreserveRatio());
    }

    @Test
    void loadFontReturnsRequestedSize() {
        Font font = ResourceManager.loadFont(22);

        assertNotNull(font);
        assertEquals(22, font.getSize(), 0.01);
    }

    @Test
    void fetchCachesImages() {
        Image first = ResourceManager.loadSprite("player.png");
        Image second = ResourceManager.loadSprite("player.png");

        assertNotNull(first);
        assertSame(first, second);
    }
}

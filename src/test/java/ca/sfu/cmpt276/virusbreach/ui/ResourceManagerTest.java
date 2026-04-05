package ca.sfu.cmpt276.virusbreach.ui;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static ca.sfu.cmpt276.virusbreach.math.Position.UNIT_SIZE;
import static org.junit.jupiter.api.Assertions.*;

class ResourceManagerTest {
    @Test
    void cannotConstruct() {
        assertThrows(UnsupportedOperationException.class, ResourceManager::new);
    }

    @Test
    void spriteFallsBackToRectangle() {
        Node node = ResourceManager.sprite("fakeasset.png", Color.HOTPINK);

        assertInstanceOf(javafx.scene.shape.Rectangle.class, node);
    }

    @Test
    void spriteUsesImageView() {
        Node node = ResourceManager.sprite("player.png", Color.HOTPINK);

        assertInstanceOf(ImageView.class, node);
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
        Font font = ResourceManager.gameFont(22);

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

    @Test
    void readImageNullInvalidSource() {
        assertNull(ResourceManager.fetchImage("non-existent uri"));
    }

    @Test
    void fallbackFontInvalidSource() {
        String font = ResourceManager.fetchFont("non-existent uri");

        assertEquals(ResourceManager.FALLBACK_FONT_FAMILY, font);
    }

    @Test
    void fallbackFontNullStream() {
        String font = ResourceManager.fetchFont(null);

        assertEquals(ResourceManager.FALLBACK_FONT_FAMILY, font);
    }
}

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

    @Test
    void readImageNullThrownStream() {
        assertNull(ResourceManager.readImage(new ThrowOnCloseStream()));
    }

    @Test
    void fallbackFontThrownStream() {
        Font font = ResourceManager.readFont(new ThrowOnCloseStream(), 18);

        assertNotNull(font);
        assertEquals(18, font.getSize(), 0.01);
    }

    @Test
    void fallbackFontNullStream() {
        Font font = ResourceManager.readFont(null, 16);

        assertNotNull(font);
        assertEquals(16, font.getSize(), 0.01);
    }

    private static class ThrowOnCloseStream extends InputStream {
        private final ByteArrayInputStream buf = new ByteArrayInputStream(new byte[0]);

        @Override
        public int read() {
            return buf.read();
        }

        @Override
        public void close() throws IOException {
            throw new IOException("intended exception");
        }
    }
}

package ca.sfu.cmpt276.virusbreach.ui;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.junit.jupiter.api.Test;

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

    /**
     * Ensure that attempting to load a non-image file as an image does not crash the game.
     */
    @Test
    void readImageFromNonImage() {
        Image image;

        image = assertDoesNotThrow(() -> ResourceManager.fetchImage("/sounds/bonus.wav"));

        assertNull(image);
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

    /**
     * Ensure that attempting to load a non-font file as a font does not crash the game.
     */
    @Test
    void fallbackInvalidFont() {
        String font;

        font = assertDoesNotThrow(() -> ResourceManager.fetchFont("/sprites/player.png"));

        assertEquals(ResourceManager.FALLBACK_FONT_FAMILY, font);
    }

    /**
     * Ensure that attempting to load a non-audio file as audio does not crash the game.
     */
    @Test
    void fallbackInvalidAudioStream() {
        AudioClip clip;

        clip = assertDoesNotThrow(() -> ResourceManager.fetchAudio("../sprites/player.png"));

        assertNull(clip);
    }

    /**
     * Ensure that attempting to load a non-media file as media does not crash the game.
     */
    @Test
    void fallbackInvalidMediaStream() {
        Media media;

        media = assertDoesNotThrow(() -> ResourceManager.fetchMusic("../sprites/player.png"));

        assertNull(media);
    }

    /**
     * Ensure that attempting to load a non-existent file as media does not crash the game.
     */
    @Test
    void fallbackMissingMediaStream() {
        Media media;

        media = assertDoesNotThrow(() -> ResourceManager.fetchMusic(null));

        assertNull(media);
    }
}

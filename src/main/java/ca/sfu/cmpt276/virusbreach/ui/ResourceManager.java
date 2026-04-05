package ca.sfu.cmpt276.virusbreach.ui;

import ca.sfu.cmpt276.virusbreach.VirusBreach;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static ca.sfu.cmpt276.virusbreach.math.Position.UNIT_SIZE;

/**
 * Manages the loading of assets, data, and other non-code resources from the game package.
 * Engages in caching of loaded resources to reduce overhead and wasted memory from duplicated objects.
 */
public class ResourceManager {
    /**
     * Font family to use in case the custom game font fails to load.
     */
    public static final String FALLBACK_FONT_FAMILY = "Courier New";

    private static final Map<String, Image> IMAGE_CACHE = new HashMap<>();
    private static final Map<String, AudioClip> AUDIO_CACHE = new HashMap<>();
    private static String fontName = null;

    ResourceManager() {
        throw new UnsupportedOperationException("This class cannot be constructed");
    }

    /**
     * Creates a sprite {@code Node} for the specified asset
     * Returns a coloured rectangle if the asset is not found
     *
     * @param asset the asset path
     * @param fallbackColor colour in case asset is not found
     * @return the created sprite Node
     */
    public static Node sprite(String asset, Color fallbackColor) {
        Image image = loadSprite(asset);
        if (image == null) {
            return new Rectangle(UNIT_SIZE, UNIT_SIZE, fallbackColor);
        }

        return createImageView(image);
    }

    /**
     * Fetches an image from the specified asset path.
     * Images are cached after the first load so the file is only read once.
     *
     * @param asset the asset path
     * @return the fetched Image or null if not found
     */
    public static Image fetchImage(String asset) {
        return IMAGE_CACHE.computeIfAbsent(asset, path -> {
            String uri = getResourceURI(path);
            return uri == null ? null : new Image(uri, false);
        });
    }

    /**
     * Fetches an audio clip from the specified asset path.
     * Audio clips are cached after the first load so the file is only read once.
     *
     * @param asset the path to the audio clip
     * @return the fetched Image or null if not found
     */
    public static AudioClip fetchAudio(String asset) {
        return AUDIO_CACHE.computeIfAbsent("/sounds/" + asset, path -> {
            String uri = getResourceURI(path);
            return uri == null ? null : new AudioClip(uri);
        });
    }

    /**
     * Creates an {@code ImageView} for the specified image.
     *
     * @param image the image to display
     * @return the created ImageView
     */
    public static ImageView createImageView(Image image) {
        ImageView view = new ImageView(image);
        view.setFitWidth(UNIT_SIZE);
        view.setFitHeight(UNIT_SIZE);
        view.setPreserveRatio(false);
        return view;
    }

    /**
     * Loads an icon from the specified asset path.
     *
     * @param asset the icon asset name
     * @return the loaded Image
     */
    public static Image loadIcon(String asset) {
        return fetchImage("/icons/" + asset);
    }

    /**
     * Loads a sprite from the specified asset path.
     *
     * @param asset the sprite asset name
     * @return the loaded Image
     */
    public static Image loadSprite(String asset) {
        return fetchImage("/sprites/" + asset);
    }

    /**
     * Returns the game font at the specified size.
     *
     * @param size the font size
     * @return the game font
     */
    public static Font gameFont(double size) {
        if (fontName == null) {
            fontName = fetchFont(getResourceURI("/fonts/VCR_OSD_MONO_1.001.ttf"));
        }
        return new Font(fontName, size);
    }

    static String fetchFont(String uri) {
        String name = FALLBACK_FONT_FAMILY;
        if (uri != null) {
            Font font = Font.loadFont(uri, 0.0);
            if (font != null) {
                name = font.getName();
            }
        }
        return name;
    }

    private static String getResourceURI(String assetPath) {
        URL resource = VirusBreach.class.getResource(assetPath);
        return resource == null ? null : resource.toExternalForm();
    }
}

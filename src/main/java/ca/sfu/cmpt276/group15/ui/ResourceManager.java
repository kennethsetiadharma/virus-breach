package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.HackingGame;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static ca.sfu.cmpt276.group15.math.Position.UNIT_SIZE;

/**
 * Manages sprite images
 * Contains methods to load and create {@code ImageViews} for sprites
 */
public class ResourceManager {
    private static final Map<String, Image> IMAGE_CACHE = new HashMap<>();
    private static Font font = null;

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
    public static Image fetch(String asset) {
        if (IMAGE_CACHE.containsKey(asset)) {
            return IMAGE_CACHE.get(asset);
        }
        try (InputStream stream = HackingGame.class.getResourceAsStream(asset)) {
            if (stream != null) {
                Image image = new Image(stream);
                IMAGE_CACHE.put(asset, image);
                return image;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
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
        return fetch("/icons/" + asset);
    }

    /**
     * Loads a sprite from the specified asset path.
     *
     * @param asset the sprite asset name
     * @return the loaded Image
     */
    public static Image loadSprite(String asset) {
        return fetch("/sprites/" + asset);
    }

    /**
     * Loads the game font at the specified size.
     *
     * @param size the font size
     * @return the loaded Font
     */
    public static Font loadFont(double size) {
        if (font == null) {
            try (var stream = HackingGame.class.getResourceAsStream("/fonts/VCR_OSD_MONO_1.001.ttf")) {
                if (stream != null) font = Font.loadFont(stream, size);
            } catch (Exception ignored) {
                font = Font.font("Courier New", size);
            }
        }
        return new Font(font.getName(), size);
    }
}

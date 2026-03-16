package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.HackingGame;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static ca.sfu.cmpt276.group15.math.Position.UNIT_SIZE;

/**
 * Manages sprite images
 * Contains methods to load and create {@code ImageViews} for sprites
 */
public class ResourceManager {
    private static final Map<String, Image> cache = new HashMap<>();

    // sprite function split up since raw Image needed for animation (see AnimatedEntity)
    /**
     * Creates a sprite {@code Node} for the specified asset
     * Returns a coloured rectangle if the asset is not found
     *
     * @param asset the asset path
     * @param fallbackColor colour in case asset is not found
     * @return the created sprite Node
     */
    public static Node sprite(String asset, Color fallbackColor) {
        Image image = fetch(asset);
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
        if (cache.containsKey(asset)) {
            return cache.get(asset);
        }
        try (InputStream stream = HackingGame.class.getResourceAsStream("/sprites/" + asset)) {
            if (stream != null) {
                Image image = new Image(stream);
                cache.put(asset, image);
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
}

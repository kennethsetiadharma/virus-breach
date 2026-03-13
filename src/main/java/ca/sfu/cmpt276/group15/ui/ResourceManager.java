package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.HackingGame;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.InputStream;

import static ca.sfu.cmpt276.group15.math.Position.UNIT_SIZE;

public class ResourceManager {
    // sprite function split up since raw Image needed for animation (see AnimatedEntity)
    public static Node sprite(String asset, Color fallbackColor) {
        //todo: cache loaded resources
        Image image = fetch(asset);
        if (image == null) {
            return new Rectangle(UNIT_SIZE, UNIT_SIZE, fallbackColor);
        }

        return createImageView(image);
    }

    public static Image fetch(String asset) {
        try (InputStream stream = HackingGame.class.getResourceAsStream("/sprites/" + asset)) {
            if (stream != null) {
                return new Image(stream);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static ImageView createImageView(Image image) {
        ImageView view = new ImageView(image);
        view.setFitWidth(UNIT_SIZE);
        view.setFitHeight(UNIT_SIZE);
        view.setPreserveRatio(false);
        return view;
    }
}

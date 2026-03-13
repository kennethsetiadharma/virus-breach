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
    public static Node sprite(String asset, Color fallbackColor) {
        //todo: cache loaded resources
        try (InputStream stream = HackingGame.class.getResourceAsStream("/sprites/" + asset)) {
            if (stream == null) {
                return new Rectangle(UNIT_SIZE, UNIT_SIZE, fallbackColor);
            }

            ImageView view = new ImageView(new Image(stream));
            view.setFitWidth(UNIT_SIZE);
            view.setFitHeight(UNIT_SIZE);
            view.setPreserveRatio(false);
            return view;
        } catch (Exception e) {
            return new Rectangle(UNIT_SIZE, UNIT_SIZE, fallbackColor);
        }
    }
}

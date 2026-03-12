package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.HackingGame;
import ca.sfu.cmpt276.group15.board.entity.Entity;
import ca.sfu.cmpt276.group15.math.Position;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.InputStream;

import static ca.sfu.cmpt276.group15.math.Position.UNIT_SIZE;

public class EntityNode<T extends Entity> extends Parent {
    private final T entity;
    private final Node base;

    public EntityNode(T entity, String sprite, Color fallback) {
        this.entity = entity;

        this.base = sprite(sprite, fallback);
        this.getChildren().add(base);
    }

    public void updateFrom(T entity) {
        this.base.setTranslateX(Position.fromGrid(entity.getPosition().x()));
        this.base.setTranslateY(Position.fromGrid(entity.getPosition().y()));
    }

    public static Node sprite(String asset, Color fallbackColor) {
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

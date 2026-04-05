package ca.sfu.cmpt276.virusbreach.ui;

import ca.sfu.cmpt276.virusbreach.board.entity.Entity;
import ca.sfu.cmpt276.virusbreach.math.Position;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * Simple render node that just has a single sprite.
 *
 * @param <T> the type of entity being rendered
 */
public class SimpleRenderNode<T extends Entity> extends RenderNode<T> {
    /**
     * Creates a simple render node for the given object and sprite.
     *
     * @param object the object being rendered
     * @param node the sprite to render
     */
    public SimpleRenderNode(T object, Node node) {
        super(object);
        this.getChildren().add(node);
    }

    /**
     * Creates a factory that creates simple render node with the specified sprite.
     *
     * @param asset the location of the sprite to use
     * @param fallback the color to use if the sprite cannot be loaded
     * @return a new render factory
     * @param <T> the type of entity being rendered
     */
    public static <T extends Entity> RenderNodeRegistry.RenderNodeFactory<T> factory(String asset, Color fallback) {
        return e -> new SimpleRenderNode<>(e, ResourceManager.sprite(asset, fallback));
    }

    @Override
    public void synchronize() {
        this.setTranslateX(Position.fromGrid(this.object.getPosition().x()));
        this.setTranslateY(Position.fromGrid(this.object.getPosition().y()));
    }
}

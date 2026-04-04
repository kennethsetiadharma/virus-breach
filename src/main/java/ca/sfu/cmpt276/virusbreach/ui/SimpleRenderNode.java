package ca.sfu.cmpt276.virusbreach.ui;

import ca.sfu.cmpt276.virusbreach.board.entity.Entity;
import ca.sfu.cmpt276.virusbreach.math.Position;
import javafx.scene.Node;

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

    @Override
    public void synchronize() {
        this.setTranslateX(Position.fromGrid(this.object.getPosition().x()));
        this.setTranslateY(Position.fromGrid(this.object.getPosition().y()));
    }
}

package ca.sfu.cmpt276.virusbreach.ui.node;

import javafx.scene.Parent;

/**
 * Representation of an object on the board.
 *
 * @param <T> the type of object to represent
 */
public abstract class RenderNode<T> extends Parent {
    /**
     * The object of interest being rendered.
     */
    protected final T object;

    /**
     * Creates a new render node for the given object.
     *
     * @param object the thing being rendered
     */
    public RenderNode(T object) {
        this.object = object;
    }

    /**
     * Reads the linked {@link #object}'s state and update what is rendered.
     */
    public abstract void synchronize();

    /**
     * {@return the object being rendered}
     */
    public T getObject() {
        return object;
    }
}

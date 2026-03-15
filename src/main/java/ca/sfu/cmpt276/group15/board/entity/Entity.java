package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.math.Direction;
import ca.sfu.cmpt276.group15.math.Position;
import javafx.scene.Node;

/**
 * An entity is a dynamic object on board. Unlike a tile, it is stateful.
 */
public abstract class Entity {
    /**
     * The board that the entity is located on.
     */
    protected final Board board;
    /**
     * The entity's position <i>prior</i> to the current tick.
     */
    private Position prevPosition;
    /**
     * The entity's current position
     */
    private Position position;
    /**
     * If {@code true}, the entity has been removed from the board and should not be used anymore.
     */
    private boolean removed = false;
    /**
     * The node that represents this entity on the screen.
     */
    protected Node renderNode;

    public Entity(Board board, int x, int y) {
        this(board, new Position(x, y));
    }

    public Entity(Board board, Position position) {
        this.board = board;
        this.position = position;
        this.prevPosition = position;
    }

    /**
     * Called once every game update.
     */
    public void tick() {
        this.prevPosition = this.position;
    }

    /**
     * {@return a new JavaFX node to visually represent this entity on the board}
     */
    protected abstract Node createRenderNode();

    /**
     * Called when this entity is located on the same tile as another entity
     *
     * @param entity the entity that was found on the same position as this one
     */
    public void onCollideWith(Entity entity) {
    }

    /**
     * Called when the entity is removed from the board.
     */
    public void onRemove() {
        this.removed = true;
    }

    /**
     * Returns whether the entity has been removed from the board and should not be used anymore
     *
     * @return {@code true} if the entity has been removed from the board
     */
    public boolean isRemoved() {
        return removed;
    }

    /**
     * Moves the entity in the specified direction, ensuring callbacks are run.
     *
     * @param direction the direction to move in
     */
    public void move(Direction direction) {
        this.position = this.position.relative(direction);
        this.board.handleEntityMoved(this);
    }

    /**
     * Synchronizes this entity's current state to its associated render node.
     */
    public void syncToView() {
        this.renderNode.setTranslateX(Position.fromGrid(this.getPosition().x()));
        this.renderNode.setTranslateY(Position.fromGrid(this.getPosition().y()));
    }

    public Position getPosition() {
        return position;
    }

    public Position getPrevPosition() {
        return prevPosition;
    }

    /**
     * {@return the JavaFX node that visually represents this entity on the board}
     */
    public final Node getRenderNode() {
        if (this.renderNode == null) this.renderNode = this.createRenderNode();
        return this.renderNode;
    }
}

package ca.sfu.cmpt276.virusbreach.board.entity;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.math.Direction;
import ca.sfu.cmpt276.virusbreach.math.Position;

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
     * Constructs a new entity on the board at the given position.
     *
     * @param board the board to spawn on
     * @param position the location to spawn at
     */
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
     * {@return the entity's current location on the board}
     */
    public Position getPosition() {
        return position;
    }

    /**
     * {@return the entity's location on the board in the previous tick}
     */
    public Position getPrevPosition() {
        return prevPosition;
    }

    /**
     * {@return the board the entity exists on}
     */
    public Board getBoard() {
        return board;
    }
}

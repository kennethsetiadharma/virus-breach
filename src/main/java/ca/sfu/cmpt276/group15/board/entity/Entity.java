package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.math.Direction;
import ca.sfu.cmpt276.group15.math.Position;

public abstract class Entity {
    protected final Board board;
    private Position prevPosition;
    private Position position;
    private boolean removed = false;

    public Entity(Board board) {
        this.board = board;
    }

    public void tick() {
        this.prevPosition = this.position;
    }

    public abstract void render();

    public void onCollideWith(Entity entity) {
    }

    public void onRemove() {
        this.removed = true;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void move(Direction direction) {
        this.setPosition(this.position.relative(direction));
        this.board.entityMoved(this);
    }

    public Position getPosition() {
        return position;
    }

    public Position getPrevPosition() {
        return prevPosition;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}

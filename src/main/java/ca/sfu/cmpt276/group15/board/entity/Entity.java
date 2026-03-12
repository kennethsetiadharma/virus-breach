package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.math.Direction;
import ca.sfu.cmpt276.group15.math.Position;
import javafx.application.Platform;
import javafx.scene.Node;

public abstract class Entity {
    protected final Board board;
    private Position prevPosition;
    private Position position;
    private boolean removed = false;
    protected Node renderNode;

    public Entity(Board board, int x, int y) {
        this(board, new Position(x, y));
    }

    public Entity(Board board, Position position) {
        this.board = board;
        this.position = position;
        this.prevPosition = position;
    }

    public void tick() {
        this.prevPosition = this.position;
    }

    protected abstract Node renderNode();

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

    public void setPosition(Position position) {
        this.position = position;
    }

    public void removeFromWorld() {
        this.board.removeEntity(this);
    }

    public final Node initRender() {
        this.renderNode = this.renderNode();
        return this.renderNode;
    }
}

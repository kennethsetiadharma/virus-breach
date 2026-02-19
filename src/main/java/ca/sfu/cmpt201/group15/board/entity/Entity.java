package ca.sfu.cmpt201.group15.board.entity;

import ca.sfu.cmpt201.group15.board.Board;
import ca.sfu.cmpt201.group15.math.Direction;
import ca.sfu.cmpt201.group15.math.Position;

public abstract class Entity {
    protected final Board board;
    private Position position;
    private int id;

    public Entity(Board board) {
        this.board = board;
    }

    public abstract void tick();

    public abstract void render();

    public void onCollideWith(Entity entity) {
    }

    public void onRemove() {
    }

    public void move(Direction direction) {
        this.setPosition(this.position.relative(direction));
        this.board.checkCollision(this);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

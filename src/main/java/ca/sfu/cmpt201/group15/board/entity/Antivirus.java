package ca.sfu.cmpt201.group15.board.entity;

import ca.sfu.cmpt201.group15.board.Board;

public class Antivirus extends Enemy {
    public Antivirus(Board board) {
        super(board);
    }

    @Override
    public void onCollideWith(Entity entity) {
        super.onCollideWith(entity);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render() {

    }
}

package ca.sfu.cmpt201.group15.board.entity;

import ca.sfu.cmpt201.group15.board.Board;

public class Player extends Entity {
    private int dataCollected = 0;

    public Player(Board board) {
        super(board);
    }

    public void adjustData(int amount) {
        this.dataCollected += amount;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render() {

    }
}

package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;

public class SourceCode extends Reward {
    private final int ttl;

    public SourceCode(Board board, int ttl) {
        super(board, 250);
        this.ttl = ttl;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render() {

    }
}

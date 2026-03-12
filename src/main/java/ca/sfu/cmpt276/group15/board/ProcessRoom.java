package ca.sfu.cmpt276.group15.board;

import ca.sfu.cmpt276.group15.math.Position;

public class ProcessRoom extends Room {
    public ProcessRoom(int x, int y, int width, int height, Position entrance) {
        super(x, y, width, height, entrance);
    }

    @Override
    public boolean furnishRoom(Board board) {
        generateInternalLayout(board);
        spawnEntities(board);
        return true;
    }

    @Override
    protected void spawnEntities(Board board) {
        // None for now
    }

    @Override
    protected void generateInternalLayout(Board board) {
        // No internal walls for now
    }
}

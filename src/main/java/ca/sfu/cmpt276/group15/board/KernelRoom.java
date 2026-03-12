package ca.sfu.cmpt276.group15.board;

import ca.sfu.cmpt276.group15.math.Position;
import javafx.geometry.Point2D;

public class KernelRoom extends Room {
    public KernelRoom(int x, int y, int width, int height, Position entrance) {
        // For now, kernel room is just an empty room with no entrance
        // We can add special features later if we want
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

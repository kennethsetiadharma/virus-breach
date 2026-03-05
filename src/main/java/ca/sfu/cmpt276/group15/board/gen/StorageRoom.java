package ca.sfu.cmpt276.group15.board.gen;

import ca.sfu.cmpt276.group15.math.Position;

public class StorageRoom extends Room {
    @Override
    public boolean furnishRoom(int x, int y, int width, int height, Position entrance) {
        return false;
    }
}

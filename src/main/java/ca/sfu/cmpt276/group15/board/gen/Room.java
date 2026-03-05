package ca.sfu.cmpt276.group15.board.gen;

import ca.sfu.cmpt276.group15.math.Position;

public abstract class Room {
    public abstract boolean furnishRoom(int x, int y, int width, int height, Position entrance);
}

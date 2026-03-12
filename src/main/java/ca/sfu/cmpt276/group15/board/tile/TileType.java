package ca.sfu.cmpt276.group15.board.tile;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.entity.Entity;
import ca.sfu.cmpt276.group15.math.Position;
import javafx.scene.Node;

public abstract class TileType {
    private final boolean solid;

    public TileType(boolean solid) {
        this.solid = solid;
    }

    public void onStep(Board board, Position position, Entity entity) {}
    public void onLeave(Board board, Position position, Entity entity) {}

    public abstract Node render(Board board, Position position);
}

package ca.sfu.cmpt276.group15.board.tile;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.entity.Entity;
import ca.sfu.cmpt276.group15.math.Position;
import ca.sfu.cmpt276.group15.render.Texture;

public class SimpleTile extends Tile {
    private final Texture texture;
    private final boolean solid;

    public SimpleTile(Texture texture, boolean solid) {
        this.texture = texture;
        this.solid = solid;
    }

    @Override
    public boolean canStep(Board board, Position pos, Entity entity) {
        return !this.solid;
    }

    @Override
    public void onStep(Board board, Position pos, Entity entity) {

    }

    @Override
    public void render(Board board, Position pos) {

    }
}

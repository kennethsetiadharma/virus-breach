package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.math.Position;

public class Antivirus extends AnimatedEntity {
    // antivirus doesn't extend Enemy anymore, to make sure this is ok after movement logic

    private static final String[] MOVING_SPRITES = {
        "antivirus_moving1.png",
        "antivirus_moving2.png"
    };

    public Antivirus(Board board, int x, int y) {
        super(board, x, y);
    }

    public Antivirus(Board board, Position position) {
        super(board, position);
    }

    @Override
    public void onCollideWith(Entity entity) {
        super.onCollideWith(entity);
        if (entity instanceof Player player) {
            this.board.removeEntity(player);
            this.board.lose();
        }
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected String getIdleSpriteAsset() {
        return "antivirus.png";
    }

    @Override
    protected String[] getMovingSpriteAssets() {
        return MOVING_SPRITES;
    }
}

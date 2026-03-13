package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.math.Direction;
import ca.sfu.cmpt276.group15.math.Position;
import ca.sfu.cmpt276.group15.ui.ResourceManager;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class AnimatedEntity extends Entity {
    protected static final int ANIMATION_FRAME_INTERVAL = 4;

    private ImageView spriteView;
    private Image idleFrame;
    private Image[] movingFrames = new Image[0];

    // sprite png defaulted to right facing
    private Direction facing = Direction.RIGHT;

    public AnimatedEntity(Board board, int x, int y) {
        super(board, x, y);
    }

    public AnimatedEntity(Board board, Position position) {
        super(board, position);
    }

    @Override
    protected final Node createRenderNode() {
        this.idleFrame = ResourceManager.fetch(getIdleSpriteAsset());

        String[] movingSpriteAssets = getMovingSpriteAssets();
        this.movingFrames = new Image[movingSpriteAssets.length];
        for (int i = 0; i < movingSpriteAssets.length; i++) {
            Image frame = ResourceManager.fetch(movingSpriteAssets[i]);
            this.movingFrames[i] = frame != null ? frame : this.idleFrame;
        }

        this.spriteView = ResourceManager.createImageView(this.idleFrame);
        return this.spriteView;
    }

    @Override
    public void syncToView() {
        super.syncToView();
        if (this.spriteView == null) {
            return;
        }

        updateFacing();
        this.spriteView.setImage(selectFrame());
        this.spriteView.setScaleX(this.facing == Direction.LEFT ? -1.0 : 1.0);
    }

    private void updateFacing() {
        int change = this.getPosition().x() - this.getPrevPosition().x();
        if (change < 0) {
            this.facing = Direction.LEFT;
        } else if (change > 0) {
            this.facing = Direction.RIGHT;
        }
    }

    private Image selectFrame() {
        if (this.movingFrames.length == 0 || this.getPosition().equals(this.getPrevPosition())) {
            return this.idleFrame;
        }

        int frameIndex = (this.board.getTimePlayed() / ANIMATION_FRAME_INTERVAL) % this.movingFrames.length;
        return this.movingFrames[frameIndex];
    }

    protected abstract String getIdleSpriteAsset();

    protected abstract String[] getMovingSpriteAssets();
}

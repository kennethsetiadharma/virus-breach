package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.math.Direction;
import ca.sfu.cmpt276.group15.math.Position;
import ca.sfu.cmpt276.group15.ui.ResourceManager;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents an entity that has animated sprites for idle and moving.
 * Animation frame is determined by the entity's movement.
 * The sprite will also flip left/right based on the direction that it is facing.
 */
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

    /**
     * Update the facing direction of the sprite.
     * Checks the change in x position since the last tick to determine if the entity is moving left or right.
     */
    private void updateFacing() {
        int change = this.getPosition().x() - this.getPrevPosition().x();
        if (change < 0) {
            this.facing = Direction.LEFT;
        } else if (change > 0) {
            this.facing = Direction.RIGHT;
        }
    }

    /**
     * Select animation frame based on movement and time on the board.
     * Depending on the # of frames the animation has, time is mod into intervals to determine the current frame.
     * @return the selected animation frame
     */
    private Image selectFrame() {
        if (this.movingFrames.length == 0 || this.getPosition().equals(this.getPrevPosition())) {
            return this.idleFrame;
        }

        int frameIndex = (this.board.getTimePlayed() / ANIMATION_FRAME_INTERVAL) % this.movingFrames.length;
        return this.movingFrames[frameIndex];
    }

    /**
     * Returns the asset path for idle sprite
     * @return asset path
     */
    protected abstract String getIdleSpriteAsset();

    /**
     * Returns the asset paths in an array for moving sprites
     * @return array of asset paths
     */
    protected abstract String[] getMovingSpriteAssets();
}

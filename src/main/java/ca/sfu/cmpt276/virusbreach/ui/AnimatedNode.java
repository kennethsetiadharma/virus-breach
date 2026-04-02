package ca.sfu.cmpt276.virusbreach.ui;

import ca.sfu.cmpt276.virusbreach.board.entity.Entity;
import ca.sfu.cmpt276.virusbreach.math.Direction;
import ca.sfu.cmpt276.virusbreach.math.Position;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * A render node for an entity that has different sprites for idle and moving
 * Animation frame is determined by the entity's movement.
 * The sprite will also flip left/right based on the direction that it is facing.
 */
public class AnimatedNode<T extends Entity> extends SimpleRenderNode<T> {
    private final Node idleFrame;
    private final int frameInterval;
    private final Node[] movingFrames;
    private Direction direction;

    public AnimatedNode(T entity, String idleSprite, int frameInterval, String... movingSprites) {
        super(entity, ResourceManager.sprite("data.png", Color.GRAY));
        this.idleFrame = ResourceManager.createImageView(ResourceManager.loadSprite(idleSprite));
        this.frameInterval = frameInterval;

        this.movingFrames = new Node[movingSprites.length];
        for (int i = 0; i < movingSprites.length; i++) {
            Image frame = ResourceManager.loadSprite(movingSprites[i]);
            this.movingFrames[i] = frame != null ? ResourceManager.createImageView(frame) : this.idleFrame;
        }

        this.getChildren().add(this.idleFrame);
    }

    @Override
    public void synchronize() {
        super.synchronize();

        animate(this.object.getPosition(), this.object.getPrevPosition(), this.object.getBoard().getTimePlayed());
    }

    public void animate(Position pos, Position prevPos, int ticks) {
        updateFacing(pos, prevPos);

        Node n = selectFrame(pos, prevPos, ticks);
        n.setScaleX(this.direction == Direction.LEFT ? -1.0 : 1.0);
        this.getChildren().setAll(n);
    }

    /**
     * Update the facing direction of the sprite.
     * Checks the change in x position since the last tick to determine if the entity is moving left or right.
     */
    private void updateFacing(Position pos, Position prevPos) {
        int change = pos.x() - prevPos.x();
        if (change < 0) {
            this.direction = Direction.LEFT;
        } else if (change > 0) {
            this.direction = Direction.RIGHT;
        }
    }

    /**
     * Select animation frame based on movement and time on the board.
     * Depending on the # of frames the animation has, time is mod into intervals to determine the current frame.
     *
     * @return the selected animation frame
     */
    private Node selectFrame(Position pos, Position prevPos, int ticks) {
        if (this.movingFrames.length == 0 || pos.equals(prevPos)) {
            return this.idleFrame;
        }

        int frameIndex = (ticks / this.frameInterval) % this.movingFrames.length;
        return this.movingFrames[frameIndex];
    }
}

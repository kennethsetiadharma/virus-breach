package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.math.Direction;
import ca.sfu.cmpt276.group15.math.Position;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;

public class AnimatedNode extends Parent {
    protected static final int ANIMATION_FRAME_INTERVAL = 4;

    private final Node idleFrame;
    private final Node[] movingFrames;
    private Direction direction;

    public AnimatedNode(String idleSprite, String[] movingSprites) {
        this.idleFrame = ResourceManager.createImageView(ResourceManager.fetch(idleSprite));

        this.movingFrames = new Node[movingSprites.length];
        for (int i = 0; i < movingSprites.length; i++) {
            Image frame = ResourceManager.fetch(movingSprites[i]);
            this.movingFrames[i] = frame != null ? ResourceManager.createImageView(frame) : this.idleFrame;
        }

        this.getChildren().add(this.idleFrame);
    }

    public void animate(Position pos, Position prevPos, int ticks) {
        updateFacing(pos, prevPos);

        Node n = selectFrame(pos, prevPos, ticks);
        n.setScaleX(this.direction == Direction.LEFT ? -1.0 : 1.0);
        this.getChildren().clear();
        this.getChildren().add(n);
    }

    private void updateFacing(Position pos, Position prevPos) {
        int change = pos.x() - prevPos.x();
        if (change < 0) {
            this.direction = Direction.LEFT;
        } else if (change > 0) {
            this.direction = Direction.RIGHT;
        }
    }

    private Node selectFrame(Position pos, Position prevPos, int ticks) {
        if (this.movingFrames.length == 0 || pos.equals(prevPos)) {
            return this.idleFrame;
        }

        int frameIndex = (ticks / ANIMATION_FRAME_INTERVAL) % this.movingFrames.length;
        return this.movingFrames[frameIndex];
    }
}

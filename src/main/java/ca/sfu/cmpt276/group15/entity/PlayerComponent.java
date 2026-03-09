package ca.sfu.cmpt276.group15.entity;

import ca.sfu.cmpt276.group15.math.Direction;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.input.KeyCode;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

public class PlayerComponent extends FixedFrequencyComponent {
    private final Set<Direction> movement = EnumSet.noneOf(Direction.class);
    private long score = 0;

    public PlayerComponent() {}

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
    }

    public long getScore() {
        return this.score;
    }

    public void updateScore(long score) {
        this.score += score;
        if (score < 0) {
            this.entity.removeFromWorld();
        }
    }

    public static void initInput() {
        FXGL.getInput().addAction(MovementAction.RIGHT, KeyCode.RIGHT);
        FXGL.getInput().addAction(MovementAction.LEFT, KeyCode.LEFT);
        FXGL.getInput().addAction(MovementAction.UP, KeyCode.UP);
        FXGL.getInput().addAction(MovementAction.DOWN, KeyCode.DOWN);
    }

    @Override
    public void onUpdate() {
        Iterator<Direction> iterator = this.movement.iterator();
        if (iterator.hasNext()) {
            this.move(iterator.next());
        }
    }

    private static class MovementAction extends UserAction {
        private static final UserAction RIGHT = new MovementAction(Direction.RIGHT);
        private static final UserAction LEFT = new MovementAction(Direction.LEFT);
        private static final UserAction UP = new MovementAction(Direction.UP);
        private static final UserAction DOWN = new MovementAction(Direction.DOWN);

        private final Direction direction;

        public MovementAction(Direction direction) {
            super(direction.name().toLowerCase());
            this.direction = direction;
        }

        @Override
        protected void onActionBegin() {
            super.onActionBegin();
            FXGL.getGameWorld().getSingletonOptional(EntityType.PLAYER).ifPresent(e -> e.getComponent(PlayerComponent.class).movement.add(this.direction));
        }

        @Override
        protected void onActionEnd() {
            super.onActionEnd();
            FXGL.getGameWorld().getSingletonOptional(EntityType.PLAYER).ifPresent(e -> e.getComponent(PlayerComponent.class).movement.remove(this.direction));
        }
    }
}

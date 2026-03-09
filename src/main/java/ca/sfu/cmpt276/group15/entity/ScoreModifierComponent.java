package ca.sfu.cmpt276.group15.entity;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.CollisionHandler;

public class ScoreModifierComponent extends Component {
    private final long delta;

    public ScoreModifierComponent(long delta) {
        this.delta = delta;
    }

    public static class ScoreModifierCollisionHandler extends CollisionHandler {
        public ScoreModifierCollisionHandler(EntityType type) {
            super(EntityType.PLAYER, type);
        }

        @Override
        protected void onCollision(Entity a, Entity b) {
            super.onCollision(a, b);
            b.removeFromWorld();
            a.getComponent(PlayerComponent.class).updateScore(b.getComponent(ScoreModifierComponent.class).delta);
        }
    }
}

package ca.sfu.cmpt276.group15;

import ca.sfu.cmpt276.group15.entity.EntityType;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;

public class VirusCollisionHandler extends CollisionHandler {
    public VirusCollisionHandler() {
        super(EntityType.PLAYER, EntityType.VIRUS);
    }

    @Override
    protected void onCollision(Entity a, Entity b) {
        super.onCollision(a, b);
        a.removeFromWorld();
    }
}

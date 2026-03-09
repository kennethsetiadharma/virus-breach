package ca.sfu.cmpt276.group15;

import ca.sfu.cmpt276.group15.entity.EntityType;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;

public class AntivirusCollisionHandler extends CollisionHandler {
    public AntivirusCollisionHandler() {
        super(EntityType.PLAYER, EntityType.ANTIVIRUS);
    }

    @Override
    protected void onCollision(Entity a, Entity b) {
        super.onCollision(a, b);
        a.removeFromWorld();
    }
}

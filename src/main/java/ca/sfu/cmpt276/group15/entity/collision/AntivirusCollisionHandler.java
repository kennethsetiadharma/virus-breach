package ca.sfu.cmpt276.group15.entity.collision;

import ca.sfu.cmpt276.group15.entity.EntityType;

import ca.sfu.cmpt276.group15.entity.PlayerComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;

public class AntivirusCollisionHandler extends CollisionHandler {
    public AntivirusCollisionHandler() {
        super(EntityType.PLAYER, EntityType.ANTIVIRUS);
    }

    @Override
    protected void onCollision(Entity a, Entity b) {
        super.onCollision(a, b);
        PlayerComponent component = a.getComponent(PlayerComponent.class);
        component.updateScore(-component.getScore());
    }
}

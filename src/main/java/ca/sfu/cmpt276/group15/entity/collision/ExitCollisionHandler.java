package ca.sfu.cmpt276.group15.entity.collision;

import ca.sfu.cmpt276.group15.entity.EntityType;
import ca.sfu.cmpt276.group15.entity.PlayerComponent;
import ca.sfu.cmpt276.group15.ui.WinMenu;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;

public class ExitCollisionHandler extends CollisionHandler {
    public ExitCollisionHandler() {
        super(EntityType.PLAYER, EntityType.EXIT);
    }

    @Override
    protected void onCollisionBegin(Entity a, Entity b) {
        super.onCollisionBegin(a, b);
        if (a.getWorld().getEntitiesByType(EntityType.DATA).isEmpty()) {
            FXGL.getSceneService().pushSubScene(new WinMenu(a.getComponent(PlayerComponent.class).getScore()));
        } else {
            //TODO: UI prompt to collect all data
        }
    }
}

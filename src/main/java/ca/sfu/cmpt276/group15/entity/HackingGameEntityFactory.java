package ca.sfu.cmpt276.group15.entity;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static ca.sfu.cmpt276.group15.math.Position.UNIT_SIZE;

public class HackingGameEntityFactory implements EntityFactory {
    private static EntityBuilder entityBase(SpawnData data) {
        return FXGL.entityBuilder(data)
            .bbox(new HitBox("square", BoundingShape.box(UNIT_SIZE - 1.0, UNIT_SIZE - 1.0)));
    }

    @Spawns("player")
    public Entity createPlayer(SpawnData data) {
        return entityBase(data)
            .type(EntityType.PLAYER)
            .with(new PlayerComponent())
            .view(new Rectangle(UNIT_SIZE, UNIT_SIZE))
            .zIndex(2)
            .collidable()
            .build();
    }

    @Spawns("antivirus")
    public Entity createAntivirus(SpawnData data) {
        return entityBase(data)
            .type(EntityType.ANTIVIRUS)
            .with(new AntivirusComponent())
            .view(new Rectangle(UNIT_SIZE, UNIT_SIZE, Color.RED))
            .zIndex(2)
            .collidable()
            .build();
    }

    @Spawns("firewall")
    public Entity createFirewall(SpawnData data) {
        int damage = 10000;
        return entityBase(data)
            .type(EntityType.FIREWALL)
            .with(new ScoreModifierComponent(-damage))
            .view(new Rectangle(UNIT_SIZE, UNIT_SIZE, Color.ORANGE))
            .zIndex(1)
            .collidable()
            .build();
    }

    @Spawns("data")
    public Entity createData(SpawnData data) {
        long value = FXGLMath.random(1073741824L, 3221225472L);
        return entityBase(data)
            .type(EntityType.DATA)
            .with(new ScoreModifierComponent(value))
            .view(new Rectangle(UNIT_SIZE, UNIT_SIZE, Color.SLATEGRAY))
            .zIndex(1)
            .collidable()
            .build();
    }

    @Spawns("sourcecode")
    public Entity createSourceCode(SpawnData data) {
        long value = FXGLMath.random(134217728L, 536870912L);
        Duration ttl = Duration.seconds(FXGLMath.random(30, 80));

        return entityBase(data)
            .type(EntityType.SOURCE_CODE)
            .with(new ExpireCleanComponent(ttl))
            .with(new ScoreModifierComponent(value))
            .view(new Rectangle(UNIT_SIZE, UNIT_SIZE, Color.LIMEGREEN))
            .zIndex(1)
            .collidable()
            .build();
    }

    @Spawns("wall")
    public Entity createWall(SpawnData data) {
        return entityBase(data)
            .type(EntityType.WALL)
            .view(new Rectangle(UNIT_SIZE, UNIT_SIZE, Color.DARKSLATEGRAY))
            .collidable()
            .build();
    }

    @Spawns("door")
    public Entity createDoor(SpawnData data) {
        return entityBase(data)
            .type(EntityType.WALL)
            .view(new Rectangle(UNIT_SIZE, UNIT_SIZE, Color.BLUE))
            .collidable()
            .build();
    }

    @Spawns("floor")
    public Entity createFloor(SpawnData data) {
        return entityBase(data)
            .type(EntityType.FLOOR)
            .view(new Rectangle(UNIT_SIZE, UNIT_SIZE))
            .build();
    }

    @Spawns("entrance")
    public Entity createEntrance(SpawnData data) {
        return entityBase(data)
            .type(EntityType.ENTRANCE)
            .view(new Rectangle(UNIT_SIZE, UNIT_SIZE, Color.LIGHTSEAGREEN))
            .build();
    }

    @Spawns("exit")
    public Entity createExit(SpawnData data) {
        return entityBase(data)
            .type(EntityType.EXIT)
            .view(new Rectangle(UNIT_SIZE, UNIT_SIZE, Color.LIGHTCORAL))
            .build();
    }
}

package ca.sfu.cmpt276.group15;

import ca.sfu.cmpt276.group15.entity.*;
import ca.sfu.cmpt276.group15.math.Position;
import ca.sfu.cmpt276.group15.ui.HackingGameSceneFactory;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class HackingGame extends GameApplication {
    public static final Duration UPDATE_INTERVAL = Duration.millis(600);
    private final GameOptions options = new GameOptions();

    public HackingGame() {
    }

    public GameOptions getOptions() {
        return options;
    }

    protected void update() {
        getGameWorld().getSingletonOptional(EntityType.PLAYER).ifPresent(e -> e.getComponent(PlayerComponent.class).onUpdate());
        getGameWorld().getEntitiesByType(EntityType.ANTIVIRUS).forEach(e -> e.getComponent(AntivirusComponent.class).onUpdate());

        if (FXGLMath.random(1, 100) <= 5) {
            SpawnData data = new SpawnData(Position.fromGrid(FXGLMath.random(0, 64)), Position.fromGrid(FXGLMath.random(0, 64)));
            getGameWorld().spawn("sourcecode", data);
        }
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Hacking Game");

        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        settings.setSceneFactory(new HackingGameSceneFactory());
    }

    @Override
    protected void initGame() {
        getGameScene().getViewport().setBounds(0, 0, 1280, 720);
        getGameScene().setBackgroundColor(Color.DARKGRAY);
        getGameWorld().addEntityFactory(new HackingGameEntityFactory());

        BoardGenerator.generateBoard(getGameWorld(), 32, 32);
        getGameScene().getGameWorld().spawn("player", getGameWorld().getSingleton(EntityType.ENTRANCE).getPosition());

        getGameTimer().runAtInterval(this::update, UPDATE_INTERVAL);
    }

    @Override
    protected void initPhysics() {
        super.initPhysics();

        FXGL.getPhysicsWorld().addCollisionHandler(new AntivirusCollisionHandler());
        FXGL.getPhysicsWorld().addCollisionHandler(new ScoreModifierComponent.ScoreModifierCollisionHandler(EntityType.FIREWALL));
        FXGL.getPhysicsWorld().addCollisionHandler(new ScoreModifierComponent.ScoreModifierCollisionHandler(EntityType.DATA));
        FXGL.getPhysicsWorld().addCollisionHandler(new ScoreModifierComponent.ScoreModifierCollisionHandler(EntityType.SOURCE_CODE));
    }

    @Override
    protected void initInput() {
        super.initInput();
        PlayerComponent.initInput();
    }
}

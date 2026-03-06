package ca.sfu.cmpt276.group15.render.fxgl;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FxglApp extends GameApplication {
    private com.almasb.fxgl.entity.Entity player;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("game");
    }

    @Override
    protected void initGame() {
        getGameScene().getViewport().setBounds(0, 0, 1280, 720);

        getGameScene().setBackgroundColor(Color.DARKGRAY);

        player = entityBuilder()
                .at(5 * 48, 5 * 48)
                .view(new Rectangle(48, 48, Color.DODGERBLUE))
                .zIndex(10)
                .buildAndAttach();
    }
}
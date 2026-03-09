package ca.sfu.cmpt276.group15.ui;

import com.almasb.fxgl.app.scene.*;
import org.jetbrains.annotations.NotNull;

public class HackingGameSceneFactory extends SceneFactory {
    @NotNull
    @Override
    public FXGLMenu newGameMenu() {
        return new PauseMenu();
    }

    @NotNull
    @Override
    public IntroScene newIntro() {
        return super.newIntro();
    }

    @NotNull
    @Override
    public LoadingScene newLoadingScene() {
        return super.newLoadingScene();
    }

    @NotNull
    @Override
    public FXGLMenu newMainMenu() {
        return new TitleMenu();
    }

    @NotNull
    @Override
    public StartupScene newStartup(int width, int height) {
        return super.newStartup(width, height);
    }
}

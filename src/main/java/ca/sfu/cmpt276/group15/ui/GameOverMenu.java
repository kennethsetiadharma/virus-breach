package ca.sfu.cmpt276.group15.ui;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class GameOverMenu extends FXGLMenu {
    public GameOverMenu() {
        super(MenuType.GAME_MENU);

        // Background
        Rectangle bg = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight());
        bg.setFill(Color.rgb(0, 0, 0, 0.7));

        // Title text
        Text title = FXGL.getUIFactoryService().newText("MISSION FAILED", Color.RED, 48);
        title.setTranslateX(FXGL.getAppWidth() / 2.0 - 200);
        title.setTranslateY(200);

        // Subtitle text
        Text subtitle = FXGL.getUIFactoryService().newText("Virus Deresoluted", Color.WHITE, 24);
        subtitle.setTranslateX(FXGL.getAppWidth() / 2.0 - 150);
        subtitle.setTranslateY(260);

        // Retry button
        var btnRetry = FXGL.getUIFactoryService().newButton("Retry");
        btnRetry.setOnAction(e -> {
            FXGL.getGameController().startNewGame();
        });
        btnRetry.setTranslateX(FXGL.getAppWidth() / 2.0 - 100);
        btnRetry.setTranslateY(350);

        // Main menu button
        var btnMainMenu = FXGL.getUIFactoryService().newButton("Main Menu");
        btnMainMenu.setOnAction(e -> {
            FXGL.getGameController().gotoMainMenu();
        });
        btnMainMenu.setTranslateX(FXGL.getAppWidth() / 2.0 - 100);
        btnMainMenu.setTranslateY(410);

        getContentRoot().getChildren().addAll(bg, title, subtitle, btnRetry, btnMainMenu);
    }
}
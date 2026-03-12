package ca.sfu.cmpt276.group15.ui;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class WinMenu extends FXGLMenu {
    public WinMenu(long finalScore) {
        super(MenuType.GAME_MENU);

        // Semi-transparent dark overlay over the game world
        Rectangle bg = new Rectangle(FXGL.getAppWidth(), FXGL.getAppHeight());
        bg.setFill(Color.rgb(0, 0, 0, 0.75));

        // Main title
        Text title = FXGL.getUIFactoryService().newText("MISSION COMPLETE", Color.LIME, 48);
        title.setTranslateX(FXGL.getAppWidth() / 2.0 - 230);
        title.setTranslateY(200);

        // Subtitle
        Text subtitle = FXGL.getUIFactoryService().newText("Data extracted successfully", Color.WHITE, 24);
        subtitle.setTranslateX(FXGL.getAppWidth() / 2.0 - 190);
        subtitle.setTranslateY(260);

        // Final score display
        Text scoreText = FXGL.getUIFactoryService().newText("Final Score: " + finalScore, Color.CYAN, 28);
        scoreText.setTranslateX(FXGL.getAppWidth() / 2.0 - 130);
        scoreText.setTranslateY(320);

        // Play again button
        var btnRetry = FXGL.getUIFactoryService().newButton("Play Again");
        btnRetry.setOnAction(e -> FXGL.getGameController().startNewGame());
        btnRetry.setTranslateX(FXGL.getAppWidth() / 2.0 - 100);
        btnRetry.setTranslateY(390);

        // Main menu button
        var btnMainMenu = FXGL.getUIFactoryService().newButton("Main Menu");
        btnMainMenu.setOnAction(e -> FXGL.getGameController().gotoMainMenu());
        btnMainMenu.setTranslateX(FXGL.getAppWidth() / 2.0 - 100);
        btnMainMenu.setTranslateY(450);

        getContentRoot().getChildren().addAll(bg, title, subtitle, scoreText, btnRetry, btnMainMenu);
    }
}

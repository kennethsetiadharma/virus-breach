package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.HackingGame;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameOverMenu extends Menu {
    public GameOverMenu(HackingGame game) {
        super(game);
        // Background
        Rectangle bg = new Rectangle();
        bg.widthProperty().bind(this.widthProperty());
        bg.heightProperty().bind(this.heightProperty());
        bg.setFill(Color.rgb(0, 0, 0, 0.7));

        // Title text
        Label title = new Label("MISSION FAILED");
        title.setTextFill(Color.RED);
        title.setFont(new Font(48));
        title.setTranslateX(1280 / 2.0 - 200);
        title.setTranslateY(200);

        // Subtitle text
        Label subtitle = new Label("Virus Deresoluted");
        subtitle.setTextFill(Color.WHITE);
        subtitle.setFont(new Font(24));
        subtitle.setTranslateX(1280 / 2.0 - 150);
        subtitle.setTranslateY(260);

        // Retry button
        var btnRetry = new Button("Retry");
        btnRetry.setOnAction(e -> {
            game.startNewGame();
        });
        btnRetry.setTranslateX(1280 / 2.0 - 100);
        btnRetry.setTranslateY(350);

        // Main menu button
        var btnMainMenu = new Button("Main Menu");
        btnMainMenu.setOnAction(e -> {
            this.getScene().setRoot(new TitleMenu(game));
        });
        btnMainMenu.setTranslateX(1280 / 2.0 - 100);
        btnMainMenu.setTranslateY(410);

        this.getChildren().addAll(bg, title, subtitle, btnRetry, btnMainMenu);
    }
}

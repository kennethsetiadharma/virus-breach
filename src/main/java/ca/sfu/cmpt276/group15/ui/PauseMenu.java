package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.HackingGame;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class PauseMenu extends Menu {
    public PauseMenu(HackingGame game, Runnable onResume) {
        super(game);

        // Semi-transparent overlay so the game is still visible behind
        Rectangle bg = new Rectangle();
        bg.widthProperty().bind(this.widthProperty());
        bg.heightProperty().bind(this.heightProperty());
        bg.setFill(Color.rgb(0, 0, 0, 0.6));

        Label title = new Label("PAUSED");
        title.setTextFill(Color.WHITE);
        title.setFont(new Font(48));
        title.setTranslateX(1280 / 2.0 - 90);
        title.setTranslateY(200);

        Button btnResume = new Button("Resume");
        btnResume.setOnAction(e -> onResume.run());
        btnResume.setTranslateX(1280 / 2.0 - 50);
        btnResume.setTranslateY(320);

        Button btnMainMenu = new Button("Main Menu");
        btnMainMenu.setOnAction(e -> game.openMenu(new TitleMenu(game)));
        btnMainMenu.setTranslateX(1280 / 2.0 - 50);
        btnMainMenu.setTranslateY(380);

        this.getChildren().addAll(bg, title, btnResume, btnMainMenu);
    }
}

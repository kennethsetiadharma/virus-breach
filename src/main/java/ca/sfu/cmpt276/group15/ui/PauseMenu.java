package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.HackingGame;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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
        title.setFont(Menu.loadFont(48));

        Button btnResume = new Button("RESUME");
        btnResume.setGraphic(Menu.iconView("resume.png"));
        btnResume.setFont(Menu.loadFont(22));
        btnResume.setStyle(BUTTON_STYLE);
        btnResume.setGraphicTextGap(14);
        btnResume.setOnAction(e -> onResume.run());

        Button btnMainMenu = new Button("EXIT");
        btnMainMenu.setGraphic(Menu.iconView("exit.png"));
        btnMainMenu.setFont(Menu.loadFont(22));
        btnMainMenu.setStyle(BUTTON_STYLE);
        btnMainMenu.setGraphicTextGap(14);
        btnMainMenu.setOnAction(e -> game.openMenu(new TitleMenu(game)));

        VBox layout = new VBox(24, title, btnResume, btnMainMenu);
        layout.setAlignment(Pos.CENTER);
        layout.prefWidthProperty().bind(this.widthProperty());
        layout.prefHeightProperty().bind(this.heightProperty());

        this.getChildren().addAll(bg, layout);
    }
}

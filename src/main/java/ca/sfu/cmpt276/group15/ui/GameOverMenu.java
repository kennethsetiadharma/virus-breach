package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.HackingGame;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameOverMenu extends Menu {
    public GameOverMenu(HackingGame game, int timePlayed) {
        super(game);

        // Full black background
        Rectangle bg = new Rectangle();
        bg.widthProperty().bind(this.widthProperty());
        bg.heightProperty().bind(this.heightProperty());
        bg.setFill(Color.BLACK);

        // Lock icon
        var lockIcon = Menu.iconView("lock.png");
        lockIcon.setFitWidth(64);
        lockIcon.setFitHeight(64);

        // Title
        Label title = new Label("QUARANTINED");
        title.setTextFill(Color.WHITE);
        title.setFont(Menu.loadFont(42));

        // Subtitle
        Label subtitle = new Label("YOU'VE BEEN CAUGHT BY AN ANTIVIRUS");
        subtitle.setTextFill(Color.WHITE);
        subtitle.setFont(Menu.loadFont(14));
        subtitle.setWrapText(true);
        subtitle.setMaxWidth(360);
        subtitle.setAlignment(javafx.geometry.Pos.CENTER);
        subtitle.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        // Timer row
        var timerIcon = Menu.iconView("timer.png");
        timerIcon.setFitWidth(22);
        timerIcon.setFitHeight(22);
        int minutes = timePlayed / 60;
        int seconds = timePlayed % 60;
        Label timerLabel = new Label(String.format("%02d:%02d", minutes, seconds));
        timerLabel.setTextFill(Color.WHITE);
        timerLabel.setFont(Menu.loadFont(22));
        HBox timerRow = new HBox(10, timerIcon, timerLabel);
        timerRow.setAlignment(Pos.CENTER);

        // Buttons
        Button btnQuit = new Button("QUIT");
        btnQuit.setGraphic(Menu.iconView("home.png"));
        btnQuit.setFont(Menu.loadFont(18));
        btnQuit.setStyle(BUTTON_STYLE);
        btnQuit.setGraphicTextGap(8);
        btnQuit.setOnAction(e -> game.openMenu(new TitleMenu(game)));

        Button btnRetry = new Button("RETRY");
        btnRetry.setGraphic(Menu.iconView("retry.png"));
        btnRetry.setFont(Menu.loadFont(18));
        btnRetry.setStyle(BUTTON_STYLE);
        btnRetry.setGraphicTextGap(8);
        btnRetry.setOnAction(e -> game.startNewGame());

        HBox buttons = new HBox(24, btnQuit, btnRetry);
        buttons.setAlignment(Pos.CENTER);

        // Center card
        VBox card = new VBox(16, lockIcon, title, subtitle, timerRow, buttons);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(480);
        card.setPadding(new Insets(40));
        card.setBackground(new Background(new BackgroundFill(Color.rgb(10, 10, 10), null, null)));

        card.layoutXProperty().bind(this.widthProperty().subtract(card.maxWidthProperty()).divide(2));
        card.layoutYProperty().bind(this.heightProperty().subtract(400).divide(2));

        this.getChildren().addAll(bg, card);
    }
}

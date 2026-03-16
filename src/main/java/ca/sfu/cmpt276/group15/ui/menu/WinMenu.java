package ca.sfu.cmpt276.group15.ui.menu;

import ca.sfu.cmpt276.group15.HackingGame;
import ca.sfu.cmpt276.group15.ui.AudioManager;
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

/**
 * Screen shown when the player wins the game.
 * Displays the score, time played, and options to retry or return to the title screen.
 */
public class WinMenu extends Menu {
    /**
     * Creates the win screen.
     *
     * @param game          the game instance
     * @param dataCollected the total data collected by the player
     * @param timePlayed    the time played in ticks
     */
    public WinMenu(HackingGame game, int dataCollected, int timePlayed) {
        super(game);

        // Full black background
        Rectangle bg = new Rectangle();
        bg.widthProperty().bind(this.widthProperty());
        bg.heightProperty().bind(this.heightProperty());
        bg.setFill(Color.BLACK);

        // Trophy icon
        var trophy = Menu.iconView("trophy.png");
        trophy.setFitWidth(64);
        trophy.setFitHeight(64);

        // Title
        Label title = new Label("INFILTRATED");
        title.setTextFill(Color.LIME);
        title.setFont(Menu.loadFont(42));

        // Subtitle
        Label subtitle = new Label("YOU'VE ACQUIRED");
        subtitle.setTextFill(Color.WHITE);
        subtitle.setFont(Menu.loadFont(16));

        // Score
        Label score = new Label(dataCollected + " GB");
        score.setTextFill(Color.LIME);
        score.setFont(Menu.loadFont(48));

        // Timer row
        var timerIcon = Menu.iconView("timer.png");
        timerIcon.setFitWidth(22);
        timerIcon.setFitHeight(22);
        int totalSeconds = timePlayed / 10;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
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
        btnQuit.setOnAction(e -> { AudioManager.play("click.wav"); game.openMenu(new TitleMenu(game)); });

        Button btnRetry = new Button("RETRY");
        btnRetry.setGraphic(Menu.iconView("retry.png"));
        btnRetry.setFont(Menu.loadFont(18));
        btnRetry.setStyle(BUTTON_STYLE);
        btnRetry.setGraphicTextGap(8);
        btnRetry.setOnAction(e -> { AudioManager.play("click.wav"); game.startNewGame(); });

        HBox buttons = new HBox(24, btnQuit, btnRetry);
        buttons.setAlignment(Pos.CENTER);

        // Center card
        VBox card = new VBox(16, trophy, title, subtitle, score, timerRow, buttons);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(480);
        card.setPadding(new Insets(40));
        card.setBackground(new Background(new BackgroundFill(Color.rgb(10, 10, 10), null, null)));

        card.layoutXProperty().bind(this.widthProperty().subtract(card.widthProperty()).divide(2));
        card.layoutYProperty().bind(this.heightProperty().subtract(card.heightProperty()).divide(2));

        this.getChildren().addAll(bg, card);
    }
}

package ca.sfu.cmpt276.virusbreach.ui.menu;

import ca.sfu.cmpt276.virusbreach.GameTime;
import ca.sfu.cmpt276.virusbreach.VirusBreach;
import ca.sfu.cmpt276.virusbreach.ui.AudioManager;
import ca.sfu.cmpt276.virusbreach.ui.ResourceManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

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
    public WinMenu(VirusBreach game, int dataCollected, int timePlayed) {
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
        title.setFont(ResourceManager.gameFont(42));

        // Subtitle
        Label subtitle = new Label("YOU'VE ACQUIRED");
        subtitle.setTextFill(Color.WHITE);
        subtitle.setFont(ResourceManager.gameFont(16));

        // Score — animates from 0 up to final value
        Label score = new Label("0 GB");
        score.setTextFill(Color.LIME);
        score.setFont(ResourceManager.gameFont(48));

        Timeline trickle = new Timeline();
        int frames = 60;
        for (int i = 1; i <= frames; i++) {
            int displayed = (int)((i / (double) frames) * dataCollected);
            trickle.getKeyFrames().add(new KeyFrame(
                Duration.millis(i * (1500.0 / frames)),
                e -> score.setText(displayed + " GB")
            ));
        }
        trickle.getKeyFrames().add(new KeyFrame(Duration.millis(1500), e -> score.setText(dataCollected + " GB")));
        trickle.play();

        // Timer row
        var timerIcon = Menu.iconView("timer.png");
        timerIcon.setFitWidth(22);
        timerIcon.setFitHeight(22);
        Label timerLabel = new Label(GameTime.ticksToClock(timePlayed));
        timerLabel.setTextFill(Color.WHITE);
        timerLabel.setFont(ResourceManager.gameFont(22));
        HBox timerRow = new HBox(10, timerIcon, timerLabel);
        timerRow.setAlignment(Pos.CENTER);

        // Buttons
        Button btnQuit = new Button("QUIT");
        btnQuit.setGraphic(Menu.iconView("home.png"));
        btnQuit.setFont(ResourceManager.gameFont(18));
        btnQuit.setStyle(BUTTON_STYLE);
        btnQuit.setGraphicTextGap(8);
        btnQuit.setOnAction(e -> { AudioManager.play("click.wav"); game.openMenu(new TitleMenu(game)); });

        Button btnRetry = new Button("RETRY");
        btnRetry.setGraphic(Menu.iconView("retry.png"));
        btnRetry.setFont(ResourceManager.gameFont(18));
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

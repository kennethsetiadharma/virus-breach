package ca.sfu.cmpt276.virusbreach.ui.menu;

import ca.sfu.cmpt276.virusbreach.GameTime;
import ca.sfu.cmpt276.virusbreach.VirusBreach;
import ca.sfu.cmpt276.virusbreach.ui.AudioManager;
import ca.sfu.cmpt276.virusbreach.ui.ResourceManager;
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
 * Screen shown when the player loses the game.
 * Displays the time played and options to retry or return to the title screen.
 */
public class GameOverMenu extends Menu {
    /**
     * Creates the game over screen.
     *
     * @param game       the game instance
     * @param timePlayed the time played in ticks
     */
    public GameOverMenu(VirusBreach game, int timePlayed) {
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
        title.setTextFill(Color.RED);
        title.setFont(ResourceManager.loadFont(42));

        // Subtitle
        Label subtitle = new Label("YOU'VE BEEN CAUGHT BY AN ANTIVIRUS");
        subtitle.setTextFill(Color.WHITE);
        subtitle.setFont(ResourceManager.loadFont(14));
        subtitle.setWrapText(true);
        subtitle.setMaxWidth(360);
        subtitle.setAlignment(javafx.geometry.Pos.CENTER);
        subtitle.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        // Timer row
        var timerIcon = Menu.iconView("timer.png");
        timerIcon.setFitWidth(22);
        timerIcon.setFitHeight(22);
        Label timerLabel = new Label(GameTime.ticksToClock(timePlayed));
        timerLabel.setTextFill(Color.WHITE);
        timerLabel.setFont(ResourceManager.loadFont(22));
        HBox timerRow = new HBox(10, timerIcon, timerLabel);
        timerRow.setAlignment(Pos.CENTER);

        // Buttons
        Button btnQuit = new Button("QUIT");
        btnQuit.setGraphic(Menu.iconView("home.png"));
        btnQuit.setFont(ResourceManager.loadFont(18));
        btnQuit.setStyle(BUTTON_STYLE);
        btnQuit.setGraphicTextGap(8);
        btnQuit.setOnAction(e -> { AudioManager.play("click.wav"); game.openMenu(new TitleMenu(game)); });

        Button btnRetry = new Button("RETRY");
        btnRetry.setGraphic(Menu.iconView("retry.png"));
        btnRetry.setFont(ResourceManager.loadFont(18));
        btnRetry.setStyle(BUTTON_STYLE);
        btnRetry.setGraphicTextGap(8);
        btnRetry.setOnAction(e -> { AudioManager.play("click.wav"); game.startNewGame(); });

        HBox buttons = new HBox(24, btnQuit, btnRetry);
        buttons.setAlignment(Pos.CENTER);

        // Center card
        VBox card = new VBox(16, lockIcon, title, subtitle, timerRow, buttons);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(480);
        card.setPadding(new Insets(40));
        card.setBackground(new Background(new BackgroundFill(Color.rgb(10, 10, 10), null, null)));

        card.layoutXProperty().bind(this.widthProperty().subtract(card.widthProperty()).divide(2));
        card.layoutYProperty().bind(this.heightProperty().subtract(card.heightProperty()).divide(2));

        this.getChildren().addAll(bg, card);
    }
}

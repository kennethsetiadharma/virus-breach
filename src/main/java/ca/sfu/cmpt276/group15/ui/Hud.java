package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.entity.Entity;
import ca.sfu.cmpt276.group15.board.entity.Player;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * HUD overlay displayed on top of the game view during gameplay.
 * Shows the elapsed timer, pause button, player score, and data packet icons.
 * Must be updated every frame via {@link #update(Board, int)}.
 */
public class Hud extends AnchorPane {

    // Must match how many Data entities BoardGenerator spawns
    private static final int TOTAL_DATA = 6;
    private static final int ICON_SIZE = 34;

    private final Text timerLabel;
    private final Text scoreLabel;
    private final ImageView[] dataIcons;

    private final Image dataEmptyImage;
    private final Image dataFullImage;

    // Track score to detect changes for popup
    private int lastScore = 0;
    private boolean bannerShown = false;

    // Cached font so we don't read from disk on every score change
    private final Font popupFont;
    private final Runnable onDamage;

    // Fixed pixel position of the data box for anchoring popups
    private static final double DATA_BOX_LEFT = 16.0;
    private static final double DATA_BOX_BOTTOM = 50.0;

    /**
     * Creates the HUD and builds all overlay nodes.
     *
     * @param onPause  called when the pause button is clicked
     * @param onDamage called when the player's score decreases
     */
    public Hud(Runnable onPause, Runnable onDamage) {
        this.onDamage = onDamage;
        var font = Menu.loadFont(20);
        var fontLarge = Menu.loadFont(28);
        this.popupFont = Menu.loadFont(22);

        this.dataEmptyImage = Menu.loadIcon("data.png");
        this.dataFullImage = Menu.loadIcon("data_completed.png");

        // --- Top-right: [timer icon] [MM:SS] [pause button] ---
        ImageView timerIcon = Menu.iconView("timer.png");

        this.timerLabel = new Text("00:00");
        this.timerLabel.setFont(fontLarge);
        this.timerLabel.setFill(Color.WHITE);

        Button pauseBtn = new Button("", Menu.iconView("pause.png"));
        pauseBtn.setOnAction(e -> onPause.run());
        pauseBtn.setStyle("-fx-background-color: transparent; -fx-padding: 2; -fx-cursor: hand;");

        HBox timerBox = new HBox(8, timerIcon, this.timerLabel, pauseBtn);
        timerBox.setStyle(
            "-fx-background-color: rgba(0,0,0,0.6);" +
            "-fx-padding: 6 12 6 12;" +
            "-fx-background-radius: 4;" +
            "-fx-alignment: center-left;"
        );
        AnchorPane.setTopAnchor(timerBox, 12.0);
        AnchorPane.setRightAnchor(timerBox, 16.0);

        // --- Bottom-left: "DATA  [score]" + row of data packet icons ---
        Text dataLabel = new Text("DATA");
        dataLabel.setFont(font);
        dataLabel.setFill(Color.WHITE);

        this.scoreLabel = new Text("0");
        this.scoreLabel.setFont(fontLarge);
        this.scoreLabel.setFill(Color.LIME);

        HBox scoreRow = new HBox(10, dataLabel, this.scoreLabel);
        scoreRow.setStyle("-fx-alignment: center-left;");

        this.dataIcons = new ImageView[TOTAL_DATA];
        HBox iconRow = new HBox(4);
        for (int i = 0; i < TOTAL_DATA; i++) {
            ImageView iv = new ImageView(this.dataEmptyImage);
            iv.setFitWidth(ICON_SIZE);
            iv.setFitHeight(ICON_SIZE);
            iv.setPreserveRatio(true);
            this.dataIcons[i] = iv;
            iconRow.getChildren().add(iv);
        }

        VBox dataBox = new VBox(4, scoreRow, iconRow);
        dataBox.setStyle(
            "-fx-background-color: rgba(0,0,0,0.6);" +
            "-fx-padding: 6 12 6 12;" +
            "-fx-background-radius: 4;"
        );
        AnchorPane.setBottomAnchor(dataBox, DATA_BOX_BOTTOM);
        AnchorPane.setLeftAnchor(dataBox, DATA_BOX_LEFT);

        // --- Center: EXIT UNLOCKED banner (hidden until all data collected) ---
        ImageView exitIcon = Menu.iconView("exit.png");
        Text bannerText = new Text("EXIT UNLOCKED");
        bannerText.setFont(Menu.loadFont(20));
        bannerText.setFill(Color.LIME);

        HBox banner = new HBox(10, exitIcon, bannerText);
        banner.setAlignment(Pos.CENTER);
        banner.setStyle(
            "-fx-background-color: rgba(0,0,0,0.8);" +
            "-fx-padding: 10 24 10 24;" +
            "-fx-background-radius: 6;" +
            "-fx-border-color: lime;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 6;"
        );
        banner.setOpacity(0);
        AnchorPane.setTopAnchor(banner, 60.0);
        AnchorPane.setLeftAnchor(banner, 0.0);
        AnchorPane.setRightAnchor(banner, 0.0);
        banner.setMouseTransparent(true);

        this.getChildren().addAll(timerBox, dataBox, banner);
        this.setPickOnBounds(false);

        this.banner = banner;
    }

    private final HBox banner;

    /**
     * Called every frame from GameMenu's AnimationTimer.
     * @param board         the active board (for timer)
     * @param dataCollected how many Data packets the player has collected so far
     */
    public void update(Board board, int dataCollected) {
        // Timer: 10 ticks per second (100ms per tick)
        int totalSeconds = board.getTimePlayed() / 10;
        this.timerLabel.setText(String.format("%02d:%02d", totalSeconds / 60, totalSeconds % 60));

        // Score
        Entity playerEntity = board.getFirstEntityMatching(e -> e instanceof Player);
        if (playerEntity instanceof Player player) {
            int currentScore = player.getDataCollected();

            // Show popup if score changed
            if (currentScore != lastScore) {
                int delta = currentScore - lastScore;
                showScorePopup(delta);
                lastScore = currentScore;
            }

            this.scoreLabel.setText(String.valueOf(currentScore));
        }

        // Data icons: first `dataCollected` slots show collected icon
        for (int i = 0; i < TOTAL_DATA; i++) {
            this.dataIcons[i].setImage(i < dataCollected ? this.dataFullImage : this.dataEmptyImage);
        }

        // Show EXIT UNLOCKED banner once when all data collected
        if (dataCollected >= TOTAL_DATA && !bannerShown) {
            bannerShown = true;
            showBanner();
        }
    }

    /**
     * Fades in the exit unlocked banner, holds it, then fades it out.
     * Only called once when all data packets have been collected.
     */
    private void showBanner() {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), banner);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1.0);

        PauseTransition hold = new PauseTransition(Duration.millis(2500));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), banner);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0);

        new SequentialTransition(fadeIn, hold, fadeOut).play();
    }

    /**
     * Shows a floating score change popup near the data box.
     * Positive delta shows in green, negative in red.
     * Also triggers the damage callback if delta is negative.
     *
     * @param delta the score change amount
     */
    private void showScorePopup(int delta) {
        if (delta < 0) onDamage.run();
        String text = delta > 0 ? "+" + delta : String.valueOf(delta);
        Color color = delta > 0 ? Color.LIME : Color.RED;

        Text popup = new Text(text);
        popup.setFont(this.popupFont);
        popup.setFill(color);
        popup.setOpacity(1.0);

        // Position near the score label — above the data box
        AnchorPane.setBottomAnchor(popup, DATA_BOX_BOTTOM + 80);
        AnchorPane.setLeftAnchor(popup, DATA_BOX_LEFT + 60);
        this.getChildren().add(popup);

        FadeTransition fade = new FadeTransition(Duration.millis(1200), popup);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        TranslateTransition rise = new TranslateTransition(Duration.millis(1200), popup);
        rise.setByY(-40);

        ParallelTransition anim = new ParallelTransition(fade, rise);
        anim.setOnFinished(e -> this.getChildren().remove(popup));
        anim.play();
    }
}

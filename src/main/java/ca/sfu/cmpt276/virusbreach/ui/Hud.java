package ca.sfu.cmpt276.virusbreach.ui;

import ca.sfu.cmpt276.virusbreach.BoardGenerator;
import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.board.entity.Player;
import ca.sfu.cmpt276.virusbreach.ui.menu.Menu;
import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * HUD overlay displayed on top of the game view during gameplay.
 * Shows the elapsed timer, pause button, player score, and data packet icons.
 * Must be updated every frame via {@link #update(Board, int)}.
 */
public class Hud extends AnchorPane {
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

    // Freeze progress bar
    private final Rectangle freezeBar;

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
        var font = ResourceManager.loadFont(20);
        var fontLarge = ResourceManager.loadFont(28);
        this.popupFont = ResourceManager.loadFont(22);

        this.dataEmptyImage = ResourceManager.loadIcon("data.png");
        this.dataFullImage = ResourceManager.loadIcon("data_completed.png");

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

        this.dataIcons = new ImageView[BoardGenerator.TOTAL_DATA];
        HBox iconRow = new HBox(4);
        for (int i = 0; i < BoardGenerator.TOTAL_DATA; i++) {
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

        HBox banner = buildBanner("EXIT UNLOCKED", "exit.png", "lime", 60.0);
        HBox serverBanner = buildBanner("SERVER ROOM UNLOCKED", "lock.png", "cyan", 60.0);
        HBox freezeBanner = buildBanner("FIREWALLS FROZEN", "freeze_token.png", "#88ccff", 60.0, true);

        // Freeze progress bar — shown bottom-left while freeze is active
        Rectangle freezeBar = new Rectangle(120, 8, Color.web("#88ccff"));
        freezeBar.setOpacity(0);
        freezeBar.setMouseTransparent(true);
        AnchorPane.setBottomAnchor(freezeBar, DATA_BOX_BOTTOM + 102);
        AnchorPane.setLeftAnchor(freezeBar, DATA_BOX_LEFT);

        this.getChildren().addAll(timerBox, dataBox, banner, serverBanner, freezeBanner, freezeBar);
        this.setPickOnBounds(false);

        this.banner = banner;
        this.serverBanner = serverBanner;
        this.freezeBanner = freezeBanner;
        this.freezeBar = freezeBar;
    }

    private HBox buildBanner(String text, String iconAsset, String borderColor, double topAnchor) {
        return buildBanner(text, iconAsset, borderColor, topAnchor, false);
    }

    private HBox buildBanner(String text, String iconAsset, String borderColor, double topAnchor, boolean isSprite) {
        Text bannerText = new Text(text);
        bannerText.setFont(ResourceManager.loadFont(20));
        bannerText.setFill(Color.web(borderColor));

        ImageView icon = new ImageView(isSprite ? ResourceManager.loadSprite(iconAsset) : ResourceManager.loadIcon(iconAsset));
        icon.setFitWidth(28);
        icon.setFitHeight(28);
        icon.setPreserveRatio(true);

        HBox box = new HBox(10, icon, bannerText);
        box.setAlignment(Pos.CENTER);
        box.setStyle(
            "-fx-background-color: rgba(0,0,0,0.8);" +
            "-fx-padding: 10 24 10 24;" +
            "-fx-background-radius: 6;" +
            "-fx-border-color: " + borderColor + ";" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 6;"
        );
        box.setOpacity(0);
        AnchorPane.setTopAnchor(box, topAnchor);
        AnchorPane.setLeftAnchor(box, 0.0);
        AnchorPane.setRightAnchor(box, 0.0);
        box.setMouseTransparent(true);
        return box;
    }

    private final HBox banner;
    private final HBox serverBanner;
    private final HBox freezeBanner;

    /**
     * Called every frame from GameMenu's AnimationTimer.
     *
     * @param board         the active board (for timer)
     * @param dataCollected how many Data packets the player has collected so far
     */
    public void update(Board board, int dataCollected) {
        // Timer: 10 ticks per second (100ms per tick)
        int totalSeconds = board.getTimePlayed() / 10;
        this.timerLabel.setText(String.format("%02d:%02d", totalSeconds / 60, totalSeconds % 60));

        // Score
        var player = (Player)board.getFirstEntityMatching(e -> e instanceof Player);
        if (player != null) {
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
        for (int i = 0; i < BoardGenerator.TOTAL_DATA; i++) {
            this.dataIcons[i].setImage(i < dataCollected ? this.dataFullImage : this.dataEmptyImage);
        }

        // Show EXIT UNLOCKED banner once when all data collected
        if (dataCollected >= BoardGenerator.TOTAL_DATA && !bannerShown) {
            bannerShown = true;
            showBanner();
        }
    }

    /**
     * Fades in the exit unlocked banner, holds it, then fades it out.
     * Only called once when all data packets have been collected.
     */
    private void showBanner() {
        animateBanner(banner);
    }

    /**
     * Shows the server room unlocked banner.
     * Called from GameMenu when the decryption key is collected.
     */
    public void showServerRoomBanner() {
        animateBanner(serverBanner);
    }

    /**
     * Shows the firewalls frozen banner and a shrinking progress bar.
     * Called from GameMenu when the player collects a freeze token.
     */
    public void showFreezeBanner() {
        animateBanner(freezeBanner);

        freezeBar.setWidth(120);
        freezeBar.setOpacity(1);

        Timeline countdown = new Timeline(
            new KeyFrame(Duration.millis(10000), new KeyValue(freezeBar.widthProperty(), 0))
        );
        countdown.setOnFinished(e -> freezeBar.setOpacity(0));
        countdown.play();
    }

    private void animateBanner(HBox box) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), box);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1.0);

        PauseTransition hold = new PauseTransition(Duration.millis(2500));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), box);
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

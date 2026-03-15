package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.entity.Entity;
import ca.sfu.cmpt276.group15.board.entity.Player;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

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

    // Fixed pixel position of the data box for anchoring popups
    private static final double DATA_BOX_LEFT = 16.0;
    private static final double DATA_BOX_BOTTOM = 50.0;

    public Hud(Runnable onPause) {
        var font = Menu.loadFont(20);
        var fontLarge = Menu.loadFont(28);

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

        this.getChildren().addAll(timerBox, dataBox);
        this.setPickOnBounds(false);
    }

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
    }

    private void showScorePopup(int delta) {
        String text = delta > 0 ? "+" + delta : String.valueOf(delta);
        Color color = delta > 0 ? Color.LIME : Color.RED;

        Text popup = new Text(text);
        popup.setFont(Menu.loadFont(22));
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

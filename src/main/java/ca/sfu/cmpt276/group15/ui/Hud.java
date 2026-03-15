package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.HackingGame;
import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.entity.Entity;
import ca.sfu.cmpt276.group15.board.entity.Player;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Hud extends AnchorPane {

    // Must match how many Data entities BoardGenerator spawns
    private static final int TOTAL_DATA = 6;
    private static final int ICON_SIZE = 28;

    private final Text timerLabel;
    private final Text scoreLabel;
    private final ImageView[] dataIcons;

    private final Image dataEmptyImage;
    private final Image dataFullImage;

    public Hud(Runnable onPause) {
        Font font = loadFont(16);
        Font fontLarge = loadFont(22);

        this.dataEmptyImage = loadIcon("data.png");
        this.dataFullImage = loadIcon("data_completed.png");

        // --- Top-right: [timer icon] [MM:SS] [pause button] ---
        ImageView timerIcon = iconView("timer.png");

        this.timerLabel = new Text("00:00");
        this.timerLabel.setFont(fontLarge);
        this.timerLabel.setFill(Color.WHITE);

        Button pauseBtn = new Button("", iconView("pause.png"));
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
        AnchorPane.setBottomAnchor(dataBox, 16.0);
        AnchorPane.setLeftAnchor(dataBox, 16.0);

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

        // Score: raw dataCollected value from the player
        Entity playerEntity = board.getFirstEntityMatching(e -> e instanceof Player);
        if (playerEntity instanceof Player player) {
            this.scoreLabel.setText(String.valueOf(player.getDataCollected()));
        }

        // Data icons: first `dataCollected` slots show collected icon
        for (int i = 0; i < TOTAL_DATA; i++) {
            this.dataIcons[i].setImage(i < dataCollected ? this.dataFullImage : this.dataEmptyImage);
        }
    }

    private static ImageView iconView(String asset) {
        ImageView iv = new ImageView(loadIcon(asset));
        iv.setFitWidth(ICON_SIZE);
        iv.setFitHeight(ICON_SIZE);
        iv.setPreserveRatio(true);
        return iv;
    }

    private static Image loadIcon(String asset) {
        try (var stream = HackingGame.class.getResourceAsStream("/icons/" + asset)) {
            if (stream != null) return new Image(stream);
        } catch (Exception ignored) {}
        return null;
    }

    private static Font loadFont(double size) {
        try (var stream = HackingGame.class.getResourceAsStream("/fonts/VCR_OSD_MONO_1.001.ttf")) {
            if (stream != null) return Font.loadFont(stream, size);
        } catch (Exception ignored) {}
        return Font.font("Courier New", size);
    }
}

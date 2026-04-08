package ca.sfu.cmpt276.virusbreach.ui.menu;

import ca.sfu.cmpt276.virusbreach.GameTime;
import ca.sfu.cmpt276.virusbreach.VirusBreach;
import ca.sfu.cmpt276.virusbreach.ui.ResourceManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Handles display and player interaction with the game.
 */
public class Menu extends Pane {
    /**
     * The associated game instance.
     */
    protected final VirusBreach game;

    /**
     * Shared JavaFX CSS styling used for buttons.
     */
    public static final String BUTTON_STYLE = """
        -fx-background-color: transparent;
        -fx-text-fill: white;
        -fx-border-color: white;
        -fx-border-width: 3;
        -fx-border-radius: 14;
        -fx-background-radius: 14;
        -fx-padding: 14 24 14 24;
        """;

    /**
     * Creates a new menu attached to the given game instance.
     *
     * @param game the game displaying this menu
     */
    public Menu(VirusBreach game) {
        this.game = game;
    }

    /**
     * Called immediately after the menu is opened (when it has been set as the root node of the scene).
     */
    public void onOpen() {
        Scene scene = this.getScene();
        if (scene != null) {
            scene.setOnKeyPressed(this::onKeyPressed);
            scene.setOnKeyReleased(this::onKeyReleased);
        }
    }

    /**
     * Called when the menu is going to be closed, before being detached from scene.
     * Should be used to clean up any remaining resources.
     */
    public void onClose() {
        Scene scene = this.getScene();
        if (scene != null) {
            scene.setOnKeyPressed(e -> {
            });
            scene.setOnKeyReleased(e -> {
            });
        }
    }

    /**
     * Called when a key is pressed when this menu is active.
     *
     * @param event the associated event data
     */
    public void onKeyPressed(KeyEvent event) {
    }

    /**
     * Called when a key is released when this menu is active.
     *
     * @param event the associated event data
     */
    public void onKeyReleased(KeyEvent event) {
    }

    /**
     * Creates a timer row HBox showing the clock icon and formatted time.
     * Shared by WinMenu and GameOverMenu to avoid duplicated code.
     *
     * @param timePlayed the time played in ticks
     * @return the constructed HBox timer row
     */
    public static HBox makeTimerRow(int timePlayed) {
        ImageView timerIcon = iconView("timer.png");
        timerIcon.setFitWidth(22);
        timerIcon.setFitHeight(22);
        Label timerLabel = new Label(GameTime.ticksToClock(timePlayed));
        timerLabel.setTextFill(Color.WHITE);
        timerLabel.setFont(ResourceManager.gameFont(22));
        HBox timerRow = new HBox(10, timerIcon, timerLabel);
        timerRow.setAlignment(Pos.CENTER);
        return timerRow;
    }

    /**
     * Creates an {@code ImageView} for the specified icon asset.
     *
     * @param asset the icon asset name
     * @return the created ImageView
     */
    public static ImageView iconView(String asset) {
        ImageView iv = new ImageView(ResourceManager.loadIcon(asset));
        iv.setFitWidth(28);
        iv.setFitHeight(28);
        iv.setPreserveRatio(true);
        return iv;
    }
}

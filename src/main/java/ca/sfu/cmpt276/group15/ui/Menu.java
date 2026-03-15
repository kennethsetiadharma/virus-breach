package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.HackingGame;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

/**
 * Handles display and player interaction with the game.
 */
public class Menu extends Pane {
    protected final HackingGame game;

    public Menu(HackingGame game) {
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
}

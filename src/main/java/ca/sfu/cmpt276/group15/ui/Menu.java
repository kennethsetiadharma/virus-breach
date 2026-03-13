package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.HackingGame;
import ca.sfu.cmpt276.group15.board.entity.Entity;
import ca.sfu.cmpt276.group15.board.entity.Player;
import ca.sfu.cmpt276.group15.math.Direction;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class Menu extends Pane {
    protected final HackingGame game;

    public Menu(HackingGame game) {
        this.game = game;
    }

    public void onOpen() {
        Scene scene = this.getScene();
        if (scene != null) {
            scene.setOnKeyPressed(this::onKeyPressed);
            scene.setOnKeyReleased(this::onKeyReleased);
        }
    }

    public void onClose() {
        Scene scene = this.getScene();
        if (scene != null) {
            scene.setOnKeyPressed(e -> {});
            scene.setOnKeyReleased(e -> {});
        }
    }

    public void onKeyPressed(KeyEvent event) {
    }

    public void onKeyReleased(KeyEvent event) {
    }
}

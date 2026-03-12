package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.GameOptions;
import ca.sfu.cmpt276.group15.HackingGame;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class OptionsMenu extends Menu {
    private final GameOptions options;

    public OptionsMenu(HackingGame game, GameOptions options) {
        super(game);
        this.options = options;

        this.getChildren().add(new Rectangle(100, 100, Color.BLUEVIOLET));
        Button back = new Button("back");
        back.setOnAction(this::backPressed);
        this.getChildren().add(back);
    }

    private void backPressed(ActionEvent action) {
        this.getScene().setRoot(new TitleMenu(this.game));
    }
}

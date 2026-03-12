package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.HackingGame;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class TitleMenu extends Menu {

    public TitleMenu(HackingGame game) {
        super(game);
        Button start = new Button("start game");
        start.setOnAction(this::startClicked);

        Button options = new Button("options");
        options.setOnAction(this::optionsClicked);
        options.setTranslateY(64);
        this.getChildren().addAll(start, options);
    }

    private void optionsClicked(ActionEvent event) {
        this.getScene().setRoot(new OptionsMenu(game, game.getOptions()));
    }

    private void startClicked(ActionEvent event) {
        this.game.startNewGame();
    }
}

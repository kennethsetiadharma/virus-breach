package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.HackingGame;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;

import java.io.InputStream;

public class TitleMenu extends Menu {
    

    public TitleMenu(HackingGame game) {
        super(game);
        this.setBackground(loadBackground());

        Button start = new Button("START MISSION");
        start.setGraphic(Menu.iconView("resume.png"));
        start.setFont(Menu.loadFont(22));
        start.setStyle(BUTTON_STYLE);
        start.setGraphicTextGap(10);
        start.setOnAction(this::startClicked);

        Button options = new Button("OPTIONS");
        options.setGraphic(Menu.iconView("options.png"));
        options.setFont(Menu.loadFont(22));
        options.setStyle(BUTTON_STYLE);
        options.setGraphicTextGap(10);
        options.setOnAction(this::optionsClicked);

        VBox buttonColumn = new VBox(24, start, options);
        buttonColumn.setAlignment(Pos.CENTER);
        buttonColumn.prefWidthProperty().bind(this.widthProperty());
        buttonColumn.setTranslateY(350);

        this.getChildren().add(buttonColumn);
    }

    private void optionsClicked(ActionEvent event) {
        this.getScene().setRoot(new OptionsMenu(game, game.getOptions()));
    }

    private void startClicked(ActionEvent event) {
        this.game.startNewGame();
    }

    private Background loadBackground() {
        try (InputStream stream = HackingGame.class.getResourceAsStream("/background/title.png")) {
            if (stream == null) {
                return null;
            }

            BackgroundImage image = new BackgroundImage(
                new Image(stream),
                null,
                null,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
            );
            return new Background(image);
        } catch (Exception ignored) {
            return null;
        }
    }
}

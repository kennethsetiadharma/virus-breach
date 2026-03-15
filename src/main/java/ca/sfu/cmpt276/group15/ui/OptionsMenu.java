package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.GameOptions;
import ca.sfu.cmpt276.group15.HackingGame;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class OptionsMenu extends Menu {
    private final GameOptions options;

    public OptionsMenu(HackingGame game, GameOptions options) {
        super(game);
        this.options = options;

        Font font = Menu.loadFont(16);
        Font fontLarge = Menu.loadFont(22);
        this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        Button back = new Button("back");
        back.setOnAction(this::backPressed);

        Label title = new Label("OPTIONS");
        title.setTextFill(Color.WHITE);
        title.setFont(fontLarge);

        Label volumeLabel = new Label();
        volumeLabel.setTextFill(Color.WHITE);
        volumeLabel.setFont(font);

        Slider volumeSlider = new Slider(0, 100, this.options.getVolume() * 100.0);
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setMajorTickUnit(25);

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double volume = newValue.doubleValue() / 100.0;
            this.options.setVolume(volume);
            volumeLabel.setText("Volume: " + Math.round(newValue.doubleValue()) + "%");
        });
        volumeLabel.setText("Volume: " + Math.round(volumeSlider.getValue()) + "%");

        VBox layout = new VBox(20, title, volumeLabel, volumeSlider, back);
        layout.setAlignment(Pos.CENTER);
        layout.setPrefWidth(420);
        layout.setTranslateX(430);
        layout.setTranslateY(180);

        this.getChildren().add(layout);
    }

    private void backPressed(ActionEvent action) {
        this.getScene().setRoot(new TitleMenu(this.game));
    }
}

package ca.sfu.cmpt276.virusbreach.ui.menu;

import ca.sfu.cmpt276.virusbreach.GameOptions;
import ca.sfu.cmpt276.virusbreach.VirusBreach;
import ca.sfu.cmpt276.virusbreach.ui.AudioManager;
import ca.sfu.cmpt276.virusbreach.ui.ResourceManager;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * A menu for adjusting game options.
 */
public class OptionsMenu extends Menu {
    private final GameOptions options;

    /**
     * Creates a new options menu.
     *
     * @param game  the game instance
     * @param options the game options
     */
    public OptionsMenu(VirusBreach game, GameOptions options) {
        super(game);
        this.options = options;

        this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        Button back = new Button("BACK");
        back.setGraphic(Menu.iconView("exit.png"));
        back.setFont(ResourceManager.loadFont(22));
        back.setStyle(BUTTON_STYLE);
        back.setGraphicTextGap(10);
        back.setOnAction(this::backPressed);

        Label title = new Label("OPTIONS");
        title.setTextFill(Color.WHITE);
        title.setFont(ResourceManager.loadFont(40));

        Label volumeLabel = new Label();
        volumeLabel.setTextFill(Color.WHITE);
        volumeLabel.setFont(ResourceManager.loadFont(22));

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

        Label soundCredits = new Label("Audio credits: Minecraft, Valorant, Among Us");
        soundCredits.setTextFill(Color.WHITE);
        soundCredits.setFont(ResourceManager.loadFont(14));

        VBox layout = new VBox(20, title, volumeLabel, volumeSlider, soundCredits, back);
        layout.setAlignment(Pos.CENTER);
        layout.setPrefWidth(420);
        layout.setTranslateX(430);
        layout.setTranslateY(180);

        this.getChildren().add(layout);
    }

    /**
     * Handles when the back button is pressed.
     *
     * @param action the action event
     */
    private void backPressed(ActionEvent action) {
        AudioManager.play("click.wav");
        this.game.openMenu(new TitleMenu(this.game));
    }
}

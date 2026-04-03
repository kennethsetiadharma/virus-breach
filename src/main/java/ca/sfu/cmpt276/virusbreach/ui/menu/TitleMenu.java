package ca.sfu.cmpt276.virusbreach.ui.menu;

import ca.sfu.cmpt276.virusbreach.VirusBreach;
import ca.sfu.cmpt276.virusbreach.ui.AudioManager;
import ca.sfu.cmpt276.virusbreach.ui.ResourceManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * A menu for the title screen of the game.
 */
public class TitleMenu extends Menu {
    private static final String[] BACKGROUND_FRAMES = {
        "/background/title_1.png",
        "/background/title_2.png",
        "/background/title_3.png",
        "/background/title_4.png"
    };

    private final Background[] backgroundFrames;
    private int backgroundFrameIndex = 0;
    private final Timeline backgroundAnimation;

    /**
     * Creates a new title menu.
     *
     * @param game the game instance
     */
    public TitleMenu(VirusBreach game) {
        super(game);
        this.backgroundFrames = new Background[BACKGROUND_FRAMES.length];
        for (int i = 0; i < BACKGROUND_FRAMES.length; i++) {
            this.backgroundFrames[i] = buildBackground(ResourceManager.fetch(BACKGROUND_FRAMES[i]));
        }
        this.setBackground(this.backgroundFrames[this.backgroundFrameIndex]);
        this.backgroundAnimation = new Timeline(new KeyFrame(
            Duration.millis(300),
            event -> advanceBackgroundFrame()
        ));
        this.backgroundAnimation.setCycleCount(Timeline.INDEFINITE);

        Button start = new Button("START MISSION");
        start.setGraphic(Menu.iconView("resume.png"));
        start.setFont(ResourceManager.loadFont(28));
        start.setStyle(BUTTON_STYLE);
        start.setGraphicTextGap(10);
        start.setOnAction(this::startClicked);

        Button options = new Button("OPTIONS");
        options.setGraphic(Menu.iconView("options.png"));
        options.setFont(ResourceManager.loadFont(28));
        options.setStyle(BUTTON_STYLE);
        options.setGraphicTextGap(10);
        options.setOnAction(this::optionsClicked);

        VBox buttonColumn = new VBox(24, start, options);
        buttonColumn.setAlignment(Pos.CENTER);
        buttonColumn.prefWidthProperty().bind(this.widthProperty());
        buttonColumn.setTranslateY(400);

        this.getChildren().add(buttonColumn);
    }

    @Override
    public void onOpen() {
        super.onOpen();
        this.backgroundAnimation.play();
    }

    @Override
    public void onClose() {
        this.backgroundAnimation.stop();
        super.onClose();
    }

    /**
     * Handles when the options button is pressed.
     *
     * @param event the action event
     */
    private void optionsClicked(ActionEvent event) {
        AudioManager.play("click.wav");
        this.game.openMenu(new OptionsMenu(game, game.getOptions()));
    }

    /**
     * Handles when the start button is pressed.
     *
     * @param event the action event
     */
    private void startClicked(ActionEvent event) {
        AudioManager.play("click.wav");
        this.game.startNewGame();
    }


    private void advanceBackgroundFrame() {
        this.backgroundFrameIndex = (this.backgroundFrameIndex + 1) % BACKGROUND_FRAMES.length;
        this.setBackground(this.backgroundFrames[this.backgroundFrameIndex]);
    }

    /**
     * Builds background from an image.
     * Returns a black background if the stream is {@code null}.
     *
     * @param baseImage the background image
     * @return the built background
     */
    static Background buildBackground(Image baseImage) {
        if (baseImage == null) {
            return blackBackground();
        }

        BackgroundImage image = new BackgroundImage(
            baseImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(100, 100, true, true, true, false)
        );
        return new Background(
            new BackgroundFill[]{new BackgroundFill(Color.BLACK, null, null)},
            new BackgroundImage[]{image}
        );
    }

    /**
     * Returns a black background for fallback.
     * 
     * @return a black background
     */
    private static Background blackBackground() {
        return new Background(new BackgroundFill(Color.BLACK, null, null));
    }
}

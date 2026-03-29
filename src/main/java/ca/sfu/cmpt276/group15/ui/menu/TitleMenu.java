package ca.sfu.cmpt276.group15.ui.menu;

import ca.sfu.cmpt276.group15.HackingGame;
import ca.sfu.cmpt276.group15.ui.AudioManager;
import ca.sfu.cmpt276.group15.ui.ResourceManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.InputStream;

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

    private int backgroundFrameIndex = 0;
    private final Timeline backgroundAnimation;
    
    /**
     * Creates a new title menu.
     *
     * @param game the game instance
     */
    public TitleMenu(HackingGame game) {
        super(game);
        this.setBackground(loadBackground(BACKGROUND_FRAMES[this.backgroundFrameIndex]));
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
        this.setBackground(loadBackground(BACKGROUND_FRAMES[this.backgroundFrameIndex]));
    }

    /**
     * Loads background from asset path.
     * Handles asset to stream and calls {@link #buildBackground(InputStream)}.
     * 
     * @param asset the asset path
     * @return the built background
     */
    private Background loadBackground(String asset) {
        return buildBackground(HackingGame.class.getResourceAsStream(asset));
    }

    /**
     * Builds background from input stream.
     * Returns a black background if the stream is null or an exception thrown.
     * 
     * @param stream the input stream
     * @return the built background
     */
    static Background buildBackground(InputStream stream) {
        try (InputStream ignored = stream) {
            if (stream == null) {
                return blackBackground();
            }

            BackgroundImage image = new BackgroundImage(
                new Image(stream),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
            );
            return new Background(
                new BackgroundFill[]{new BackgroundFill(Color.BLACK, null, null)},
                new BackgroundImage[]{image}
            );
        } catch (Exception ignored) {
            return blackBackground();
        }
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

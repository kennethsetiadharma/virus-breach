package ca.sfu.cmpt276.group15.ui.menu;

import ca.sfu.cmpt276.group15.HackingGame;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.Stop;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

@ExtendWith(ApplicationExtension.class)
class TitleMenuTest {
    private final HackingGame game = new HackingGame();
    private Stage stage;

    @Start
    void start(Stage stage) {
        WaitForAsyncUtils.autoCheckException = false;
        WaitForAsyncUtils.clearExceptions();
        this.stage = stage;
        this.game.start(stage);
    }

    @Test
    void canOpenOptions(FxRobot robot) {
        robot.clickOn("OPTIONS");

        WaitForAsyncUtils.waitForFxEvents();
        assertInstanceOf(OptionsMenu.class, this.stage.getScene().getRoot());
        
        // check each button
        verifyThat("OPTIONS", hasText("OPTIONS"));
        verifyThat("BACK", hasText("BACK"));
    }

    @Test
    void optionsBackReturnsToTitle(FxRobot robot) {
        robot.clickOn("OPTIONS");

        WaitForAsyncUtils.waitForFxEvents();
        robot.clickOn("BACK");

        WaitForAsyncUtils.waitForFxEvents();
        assertInstanceOf(TitleMenu.class, this.stage.getScene().getRoot());
        
        verifyThat("START MISSION", hasText("START MISSION"));
        verifyThat("OPTIONS", hasText("OPTIONS"));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, 0.25, 0.5, 0.75, 1.0})
    void optionsSliderUpdatesVolume(double volume, FxRobot robot) {
        robot.clickOn("OPTIONS");
        
        WaitForAsyncUtils.waitForFxEvents();
        Slider slider = robot.lookup(".slider").queryAs(Slider.class);
        Label volumeLabel = robot.lookup((Label label) -> label.getText() != null && label.getText().startsWith("Volume:")).queryAs(Label.class);

        Platform.runLater(() -> slider.setValue(volume * 100));
        
        // set volume and check
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(volume, this.game.getOptions().getVolume(), 0.01);
        assertEquals("Volume: " + (int)(volume * 100) + "%", volumeLabel.getText());
    }

    @Stop
    void close() {
        WaitForAsyncUtils.clearExceptions();
        if (this.stage.getScene() != null && this.stage.getScene().getRoot() instanceof Menu menu) {
            menu.onClose();
        }
        this.stage.close();
    }
}

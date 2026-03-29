package ca.sfu.cmpt276.group15.ui;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.Stop;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(ApplicationExtension.class)
class TutorialOverlayTest {
    private int callbackCount = 0;
    private Stage stage;

    @Start
    void start(Stage stage) {
        WaitForAsyncUtils.autoCheckException = false;
        WaitForAsyncUtils.clearExceptions();
        this.stage = stage;
        stage.setScene(new Scene(new TutorialOverlay(this::callbackIncrement), 1280, 720));
        stage.show();
    }

    @Test
    void showsTutorialTexts(FxRobot robot) {
        assertFalse(robot.lookup("HOW TO PLAY").tryQuery().isEmpty());
        assertFalse(robot.lookup("GOT IT").tryQuery().isEmpty());
        assertFalse(robot.lookup("Collect all 6 data packets to unlock the exit").tryQuery().isEmpty());
        assertFalse(robot.lookup("Don't get caught by the antivirus!").tryQuery().isEmpty());
    }

    @Test
    void dismissRunsCallback(FxRobot robot) {
        robot.clickOn("GOT IT");
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(1, this.callbackCount);
    }

    @Test
    void loadIconsNotSprite() throws ReflectiveOperationException {
        TutorialOverlay overlay = new TutorialOverlay(this::callbackIncrement);
        Image loaded = (Image) overlay.loadImage("pause.png", false);
        assertSame(ResourceManager.loadIcon("pause.png"), loaded);
    }

    @Stop
    void close() {
        WaitForAsyncUtils.clearExceptions();
        this.stage.close();
        try {
            FxToolkit.hideStage();
            FxToolkit.cleanupStages();
        } catch (java.util.concurrent.TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private void callbackIncrement() {
        callbackCount++;
    }
}

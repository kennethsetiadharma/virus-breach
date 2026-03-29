package ca.sfu.cmpt276.group15.ui.menu;

import ca.sfu.cmpt276.group15.HackingGame;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.Stop;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

@ExtendWith(ApplicationExtension.class)
class GameMenuTest {
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
    void startGameShowsTutorial(FxRobot robot) {
        startGame(robot);

        assertInstanceOf(GameMenu.class, this.stage.getScene().getRoot());
        assertFalse(robot.lookup("HOW TO PLAY").tryQuery().isEmpty());
        verifyThat("GOT IT", hasText("GOT IT"));
    }

    @Test
    void canDismissTutorialAfterStartingGame(FxRobot robot) {
        startGame(robot);
        dismissTutorial(robot);

        assertInstanceOf(GameMenu.class, this.stage.getScene().getRoot());

        // tutorial should be gone
        assertTrue(robot.lookup("GOT IT").tryQuery().isEmpty());
    }

    @Test
    void escActivatesPauseMenu(FxRobot robot) {
        startGame(robot);
        dismissTutorial(robot);

        robot.press(KeyCode.ESCAPE).release(KeyCode.ESCAPE);

        // pause menu should show
        WaitForAsyncUtils.waitForFxEvents();
        verifyThat("PAUSED", hasText("PAUSED"));
        verifyThat("RESUME", hasText("RESUME"));

        robot.clickOn("RESUME");

        WaitForAsyncUtils.waitForFxEvents();

        // pause menu should be gone
        assertTrue(robot.lookup("PAUSED").tryQuery().isEmpty());
    }

    @Test
    void pauseExitReturnsToTitle(FxRobot robot) {
        startGame(robot);
        dismissTutorial(robot);

        robot.press(KeyCode.ESCAPE).release(KeyCode.ESCAPE);

        WaitForAsyncUtils.waitForFxEvents();
        robot.clickOn("EXIT");

        WaitForAsyncUtils.waitForFxEvents();
        assertInstanceOf(TitleMenu.class, this.stage.getScene().getRoot());

        // title should show
        verifyThat("START MISSION", hasText("START MISSION"));
    }

    private void startGame(FxRobot robot) {
        Button startMission = robot.lookup("START MISSION").queryButton();
        robot.clickOn(startMission);
        WaitForAsyncUtils.waitForFxEvents();
    }

    private void dismissTutorial(FxRobot robot) {
        Button gotIt = robot.lookup("GOT IT").queryButton();
        robot.clickOn(gotIt);
        WaitForAsyncUtils.waitForFxEvents();
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

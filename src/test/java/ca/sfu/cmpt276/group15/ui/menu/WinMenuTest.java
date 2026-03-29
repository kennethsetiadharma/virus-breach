package ca.sfu.cmpt276.group15.ui.menu;

import ca.sfu.cmpt276.group15.HackingGame;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.Stop;
import org.testfx.util.WaitForAsyncUtils;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

@ExtendWith(ApplicationExtension.class)
class WinMenuTest {
    private final HackingGame game = new HackingGame();
    private Stage stage;

    @Start
    void start(Stage stage) {
        WaitForAsyncUtils.autoCheckException = false;
        WaitForAsyncUtils.clearExceptions();
        this.stage = stage;
        this.game.start(stage);

        this.game.openMenu(new WinMenu(this.game, 250, 615));
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void winTexts(FxRobot robot) {
        assertInstanceOf(WinMenu.class, this.stage.getScene().getRoot());
        verifyThat("INFILTRATED", hasText("INFILTRATED"));
        verifyThat("YOU'VE ACQUIRED", hasText("YOU'VE ACQUIRED"));
        verifyThat("01:01", hasText("01:01"));
        verifyThat("QUIT", hasText("QUIT"));
        verifyThat("RETRY", hasText("RETRY"));

        // wait until score animates up
        try {
            WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS,() -> !robot.lookup("250 GB").tryQuery().isEmpty());
        } catch (java.util.concurrent.TimeoutException e) {
            throw new AssertionError("score not shown in time", e);
        }

        verifyThat("250 GB", hasText("250 GB"));
    }

    @Test
    void quitReturnsToTitle(FxRobot robot) {
        robot.clickOn("QUIT");

        WaitForAsyncUtils.waitForFxEvents();
        assertInstanceOf(TitleMenu.class, this.stage.getScene().getRoot());
        verifyThat("START MISSION", hasText("START MISSION"));
    }

    @Test
    void retryStartsNewGame(FxRobot robot) {
        robot.clickOn("RETRY");

        WaitForAsyncUtils.waitForFxEvents();
        assertInstanceOf(GameMenu.class, this.stage.getScene().getRoot());
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

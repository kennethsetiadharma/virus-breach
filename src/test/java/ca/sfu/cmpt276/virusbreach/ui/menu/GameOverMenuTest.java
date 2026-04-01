package ca.sfu.cmpt276.virusbreach.ui.menu;

import ca.sfu.cmpt276.virusbreach.VirusBreach;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.Stop;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

@ExtendWith(ApplicationExtension.class)
class GameOverMenuTest {
    private final VirusBreach game = new VirusBreach();
    private Stage stage;

    @Start
    void start(Stage stage) {
        WaitForAsyncUtils.autoCheckException = false;
        WaitForAsyncUtils.clearExceptions();
        this.stage = stage;
        this.game.start(stage);

        this.game.openMenu(new GameOverMenu(this.game, 615));
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    void gameOverTexts(FxRobot robot) {
        assertInstanceOf(GameOverMenu.class, this.stage.getScene().getRoot());
        verifyThat("QUARANTINED", hasText("QUARANTINED"));
        verifyThat("YOU'VE BEEN CAUGHT BY AN ANTIVIRUS", hasText("YOU'VE BEEN CAUGHT BY AN ANTIVIRUS"));
        verifyThat("01:01", hasText("01:01"));
        verifyThat("QUIT", hasText("QUIT"));
        verifyThat("RETRY", hasText("RETRY"));
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

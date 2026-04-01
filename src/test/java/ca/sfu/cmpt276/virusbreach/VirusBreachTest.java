package ca.sfu.cmpt276.virusbreach;

import ca.sfu.cmpt276.virusbreach.ui.menu.GameMenu;
import ca.sfu.cmpt276.virusbreach.ui.menu.TitleMenu;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.Stop;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class VirusBreachTest {
    private final VirusBreach game = new VirusBreach();

    @Start
    void start(Stage stage) {
        this.game.start(stage);
    }

    @Test
    void startsOnTitleMenu(FxRobot robot) {
        assertInstanceOf(TitleMenu.class, this.game.activeMenu);
    }

    @Test
    void canStartGame(FxRobot robot) {
        Button startMission = robot.lookup("START MISSION").queryButton();

        robot.clickOn(startMission);

        assertInstanceOf(GameMenu.class, this.game.activeMenu);
    }

    @Stop
    void close() {
        this.game.stop();
    }
}

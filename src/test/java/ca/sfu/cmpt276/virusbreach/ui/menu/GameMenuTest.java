package ca.sfu.cmpt276.virusbreach.ui.menu;

import ca.sfu.cmpt276.virusbreach.VirusBreach;
import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.board.TestHelper;
import ca.sfu.cmpt276.virusbreach.board.entity.Data;
import ca.sfu.cmpt276.virusbreach.board.entity.FreezeToken;
import ca.sfu.cmpt276.virusbreach.board.entity.Player;
import ca.sfu.cmpt276.virusbreach.board.tile.OpenDoor;
import ca.sfu.cmpt276.virusbreach.board.tile.Wall;
import ca.sfu.cmpt276.virusbreach.math.Position;
import ca.sfu.cmpt276.virusbreach.ui.ResourceManager;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.Stop;
import org.testfx.util.WaitForAsyncUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

@ExtendWith(ApplicationExtension.class)
class GameMenuTest {
    private final VirusBreach game = new VirusBreach();
    private Stage stage;
    private Board board;
    private Player player;
    private GameMenu menu;

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

    @ParameterizedTest
    @CsvSource({
        "W,2,1",
        "A,1,2",
        "S,2,3",
        "D,3,2"
    })
    void playerMovement(KeyCode keyCode, int expectedX, int expectedY) {
        setUpGame(5, 5, 2, 2);

        this.menu.onKeyPressed(newKeyEvent(KeyEvent.KEY_PRESSED, keyCode));
        this.board.tick();
        this.menu.onKeyReleased(newKeyEvent(KeyEvent.KEY_RELEASED, keyCode));

        assertEquals(new Position(expectedX, expectedY), this.player.getPosition());
    }

    @Test
    void keyPressOtherThanWASD() {
        setUpGame(5, 5, 2, 2);

        this.menu.onKeyPressed(newKeyEvent(KeyEvent.KEY_PRESSED, KeyCode.Q));
        this.board.tick();

        assertEquals(new Position(2, 2), this.player.getPosition());
    }

    @Test
    void tileChangeReplaceTileNode() {
        setUpGame(5, 5, 2, 2);
        Node before = this.menu.getTileNode(new Position(1, 1));

        this.menu.onTileChanged(new Position(1, 1), Wall.INSTANCE);

        WaitForAsyncUtils.waitForFxEvents();
        Node after = this.menu.getTileNode(new Position(1, 1));

        assertNotSame(before, after);
    }

    @Test
    void tileChangeOpenServerRoom() {
        setUpGame(5, 5, 2, 2);

        this.menu.onTileChanged(new Position(1, 1), OpenDoor.INSTANCE);

        WaitForAsyncUtils.waitForFxEvents();

        double notifOpacity = collectNodes(this.menu).stream()
            .filter(Text.class::isInstance)
            .map(Text.class::cast)
            .filter(node -> "SERVER ROOM UNLOCKED".equals(node.getText()))
            .map(Node::getParent)
            .findFirst()
            .orElseThrow()
            .getOpacity();
        
        assertTrue(notifOpacity > 0.0);
    }

    @Test
    void onWinWins() {
        setUpGame(5, 5, 2, 2);
        for (int i = 0; i < 25; i++) {
            this.board.tick();
        }

        this.menu.onWin(250);
        WaitForAsyncUtils.waitForFxEvents();

        assertInstanceOf(WinMenu.class, this.stage.getScene().getRoot());
        verifyThat("INFILTRATED", hasText("INFILTRATED"));
    }

    @Test
    void onLoseLoses() {
        setUpGame(5, 5, 2, 2);
        for (int i = 0; i < 25; i++) {
            this.board.tick();
        }

        this.menu.onLose();
        WaitForAsyncUtils.waitForFxEvents();

        assertInstanceOf(GameOverMenu.class, this.stage.getScene().getRoot());
        verifyThat("QUARANTINED", hasText("QUARANTINED"));
    }

    @Test
    void damageFlash() {
        setUpGame(5, 5, 2, 2);

        this.player.adjustData(100);
        this.menu.onUpdate();

        WaitForAsyncUtils.waitForFxEvents();
        this.player.adjustData(-50);
        this.menu.onUpdate();

        WaitForAsyncUtils.waitForFxEvents();
        Rectangle damageFlash = this.menu.getDamageFlashNode();
        assertTrue(damageFlash.getOpacity() > 0.0);
    }

    @Test
    void entityRemovedData() {
        // set up game but with data
        this.board = TestHelper.createEnclosedBoard(5, 5);
        this.player = new Player(this.board, new Position(2, 2));
        Data data = new Data(this.board, new Position(1, 1));
        this.board.addEntity(this.player);
        this.board.addEntity(data);
        runOnFxThread(() -> {
            this.menu = new GameMenu(this.game, this.board);
            this.stage.getScene().setRoot(this.menu);
        });

        int count = collectNodes(this.menu).size();

        this.menu.onEntityRemoved(data);
        WaitForAsyncUtils.waitForFxEvents();
        this.menu.onUpdate();
        WaitForAsyncUtils.waitForFxEvents();

        long fullIcons = collectNodes(this.menu).stream()
            .filter(ImageView.class::isInstance)
            .map(ImageView.class::cast)
            .filter(iv -> iv.getImage() == ResourceManager.loadIcon("data_completed.png"))
            .count();

        assertEquals(1, fullIcons);
        assertEquals(count - 1, collectNodes(this.menu).size());
    }

    @Test
    void entityRemovedFreezeToken() {
        // set up game but with freeze token
        this.board = TestHelper.createEnclosedBoard(5, 5);
        this.player = new Player(this.board, new Position(2, 2));
        FreezeToken freezeToken = new FreezeToken(this.board, new Position(1, 1));
        this.board.addEntity(this.player);
        this.board.addEntity(freezeToken);
        runOnFxThread(() -> {
            this.menu = new GameMenu(this.game, this.board);
            this.stage.getScene().setRoot(this.menu);
        });

        int count = collectNodes(this.menu).size();

        this.menu.onEntityRemoved(freezeToken);
        WaitForAsyncUtils.waitForFxEvents();

        double notifOpacity = collectNodes(this.menu).stream()
            .filter(Text.class::isInstance)
            .map(Text.class::cast)
            .filter(node -> "FIREWALLS FROZEN".equals(node.getText()))
            .map(Node::getParent)
            .findFirst()
            .orElseThrow()
            .getOpacity();

        assertTrue(notifOpacity > 0.0);
        assertEquals(count - 1, collectNodes(this.menu).size());
    }

    @Test
    void playerMovementDuringPause() {
        setUpGame(5, 5, 2, 2);
        WaitForAsyncUtils.waitForAsyncFx(1000,
            () -> this.menu.onKeyPressed(newKeyEvent(KeyEvent.KEY_PRESSED, KeyCode.ESCAPE)));
        this.board.setPaused(true);

        WaitForAsyncUtils.waitForAsyncFx(1000,
            () -> this.menu.onKeyPressed(newKeyEvent(KeyEvent.KEY_PRESSED, KeyCode.D)));
        this.board.tick();

        assertEquals(new Position(2, 2), this.player.getPosition());
    }

    @Test
    void centrePlayerViewport() {
        setUpGame(3, 3, 1, 1);

        this.menu.onUpdate();
        WaitForAsyncUtils.waitForFxEvents();

        Group camera = this.menu.getCameraNode();
        double contentWidth = Position.fromGrid(this.board.width()) * VirusBreach.CAMERA_ZOOM;
        double contentHeight = Position.fromGrid(this.board.height()) * VirusBreach.CAMERA_ZOOM;
        double expectedX = (this.menu.getWidth() - contentWidth) / 2.0;
        double expectedY = (this.menu.getHeight() - contentHeight) / 2.0;

        assertEquals(expectedX, camera.getTranslateX(), 0.01);
        assertEquals(expectedY, camera.getTranslateY(), 0.01);
    }

    @Test
    void keyPressedNoPlayer() {
        setUpGameNoPlayer(5, 5);

        assertDoesNotThrow(() -> this.menu.onKeyPressed(newKeyEvent(KeyEvent.KEY_PRESSED, KeyCode.W)));
    }

    @Test
    void keyReleasedNoPlayer() {
        setUpGameNoPlayer(5, 5);

        assertDoesNotThrow(() -> this.menu.onKeyReleased(newKeyEvent(KeyEvent.KEY_RELEASED, KeyCode.W)));
    }

    @Test
    void onUpdateNoPlayer() {
        setUpGameNoPlayer(5, 5);

        this.menu.onUpdate();
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(0.0, this.menu.getCameraNode().getTranslateX(), 0.01);
        assertEquals(0.0, this.menu.getCameraNode().getTranslateY(), 0.01);
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

    private void setUpGame(int width, int height, int playerX, int playerY) {
        this.board = TestHelper.createEnclosedBoard(width, height);
        this.player = new Player(this.board, new Position(playerX, playerY));
        this.board.addEntity(this.player);

        runOnFxThread(() -> {
            this.menu = new GameMenu(this.game, this.board);
            this.stage.getScene().setRoot(this.menu);
        });
    }

    private void setUpGameNoPlayer(int width, int height) {
        this.board = TestHelper.createEnclosedBoard(width, height);
        this.player = null;

        runOnFxThread(() -> {
            this.menu = new GameMenu(this.game, this.board);
            this.stage.getScene().setRoot(this.menu);
        });
    }

    private static List<Node> collectNodes(Node node) {
        List<Node> nodes = new ArrayList<>();
        nodes.add(node);
        if (node instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                nodes.addAll(collectNodes(child));
            }
        }
        return nodes;
    }

    private static KeyEvent newKeyEvent(javafx.event.EventType<KeyEvent> type, KeyCode code) {
        return new KeyEvent(type, "", "", code, false, false, false, false);
    }

    @Stop
    void close() {
        WaitForAsyncUtils.clearExceptions();
        runOnFxThread(() -> {
            if (this.stage.getScene() != null && this.stage.getScene().getRoot() instanceof Menu menu) {
                menu.onClose();
            }
            this.stage.close();
        });
        try {
            FxToolkit.hideStage();
            FxToolkit.cleanupStages();
        } catch (java.util.concurrent.TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private static void runOnFxThread(Runnable action) {
        WaitForAsyncUtils.waitForAsyncFx(1000, action);
    }
}

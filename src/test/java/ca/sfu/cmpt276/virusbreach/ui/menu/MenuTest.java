package ca.sfu.cmpt276.virusbreach.ui.menu;

import ca.sfu.cmpt276.virusbreach.VirusBreach;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.Stop;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class MenuTest {
    private MenuForTesting menu;
    private Stage stage;

    @Start
    void start(Stage stage) {
        WaitForAsyncUtils.autoCheckException = false;
        WaitForAsyncUtils.clearExceptions();
        this.stage = stage;

        this.menu = new MenuForTesting();
        stage.setScene(new Scene(this.menu, 640, 480));
        stage.show();
    }

    @Test
    void keyHandlersInstalled() {
        this.menu.onOpen();

        this.stage.getScene().getOnKeyPressed().handle(newKeyEvent(KeyEvent.KEY_PRESSED, KeyCode.A));
        this.stage.getScene().getOnKeyReleased().handle(newKeyEvent(KeyEvent.KEY_RELEASED, KeyCode.A));

        assertEquals(1, this.menu.pressedCount);
        assertEquals(1, this.menu.releasedCount);
    }

    @Test
    void noHandlerOnClose() {
        this.menu.onOpen();
        this.menu.onClose();

        this.stage.getScene().getOnKeyPressed().handle(newKeyEvent(KeyEvent.KEY_PRESSED, KeyCode.A));
        this.stage.getScene().getOnKeyReleased().handle(newKeyEvent(KeyEvent.KEY_RELEASED, KeyCode.A));

        assertEquals(0, this.menu.pressedCount);
        assertEquals(0, this.menu.releasedCount);
    }

    @Test
    void missingDoesntThrow() {
        Menu detached = new Menu(new VirusBreach());

        assertDoesNotThrow(detached::onOpen);
        assertDoesNotThrow(detached::onClose);
    }

    @Test
    void iconUsesSpecifiedSize() {
        ImageView icon = Menu.iconView("pause.png");

        assertNotNull(icon.getImage());
        assertEquals(28.0, icon.getFitWidth());
        assertEquals(28.0, icon.getFitHeight());
        assertEquals(true, icon.isPreserveRatio());
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

    private static KeyEvent newKeyEvent(javafx.event.EventType<KeyEvent> type, KeyCode code) {
        return new KeyEvent(type, "", "", code, false, false, false, false);
    }

    private static class MenuForTesting extends Menu {
        private int pressedCount;
        private int releasedCount;

        private MenuForTesting() {
            super(new VirusBreach());
        }

        @Override
        public void onKeyPressed(KeyEvent event) {
            super.onKeyPressed(event);
            this.pressedCount++;
        }

        @Override
        public void onKeyReleased(KeyEvent event) {
            super.onKeyReleased(event);
            this.releasedCount++;
        }
    }
}

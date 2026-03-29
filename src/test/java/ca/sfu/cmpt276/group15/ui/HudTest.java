package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.TestHelper;
import ca.sfu.cmpt276.group15.board.entity.Player;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.Stop;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ApplicationExtension.class)
class HudTest {
    private Hud hud;
    private Stage stage;

    @Start
    void start(Stage stage) {
        WaitForAsyncUtils.autoCheckException = false;
        WaitForAsyncUtils.clearExceptions();
        this.stage = stage;

        this.hud = new Hud(() -> {}, () -> {});
        stage.setScene(new Scene(this.hud, 1280, 720));
        stage.show();
    }

    @Test
    void updateShowsTimeAndScore() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Player player = new Player(board, 2, 2);
        board.addEntity(player);
        player.adjustData(250);

        for (int i = 0; i < 25; i++) {
            board.tick();
        }

        WaitForAsyncUtils.waitForAsyncFx(1000, () -> this.hud.update(board, 0));

        assertFalse(findText("00:02").isEmpty());
        assertFalse(findText("250").isEmpty());
    }

    @Test
    void updateShowsExitText() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Player player = new Player(board, 2, 2);
        board.addEntity(player);

        WaitForAsyncUtils.waitForAsyncFx(1000, () -> this.hud.update(board, ca.sfu.cmpt276.group15.BoardGenerator.TOTAL_DATA));

        assertFalse(findText("EXIT UNLOCKED").isEmpty());
    }

    @Test
    void freezeBannerShowsProgressBar() {
        WaitForAsyncUtils.waitForAsyncFx(1000, this.hud::showFreezeBanner);

        // shape of progress bar
        assertTrue(this.hud.getChildren().stream()
            .filter(node -> node instanceof javafx.scene.shape.Rectangle)
            .map(node -> (javafx.scene.shape.Rectangle) node)
            .anyMatch(rect -> rect.getOpacity() > 0 && rect.getWidth() > 0 && rect.getWidth() <= 120)
        );
    }

    private java.util.List<Text> findText(String content) {
        return collectNodes(this.hud).stream().filter(Text.class::isInstance).map(Text.class::cast).filter(text -> content.equals(text.getText())).toList();
    }

    private java.util.List<Node> collectNodes(Node node) {
        java.util.List<Node> nodes = new java.util.ArrayList<>();
        nodes.add(node);
        if (node instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                nodes.addAll(collectNodes(child));
            }
        }
        return nodes;
    }

    @Stop
    void close() {
        WaitForAsyncUtils.clearExceptions();
        this.stage.close();
    }
}

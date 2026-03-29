package ca.sfu.cmpt276.group15.board.tile;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.BoardObserver;
import ca.sfu.cmpt276.group15.board.TestHelper;
import ca.sfu.cmpt276.group15.board.entity.Antivirus;
import ca.sfu.cmpt276.group15.board.entity.Data;
import ca.sfu.cmpt276.group15.board.entity.Player;
import ca.sfu.cmpt276.group15.board.entity.SourceCode;
import ca.sfu.cmpt276.group15.math.Direction;
import ca.sfu.cmpt276.group15.math.Position;
import ca.sfu.cmpt276.group15.ui.ResourceManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExitTest implements BoardObserver {
    private int gamesWon = 0;

    /**
     * Ensure that the player cannot win the game before they collect all data on the board.
     */
    @Test
    void cannotEscapeWithoutCollectingData() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        board.attach(this);
        board.setTile(2, 0, Exit.INSTANCE);
        Player player = new Player(board, 2, 1);
        board.addEntity(player);
        board.addEntity(new Data(board, 2, 2));

        player.move(Direction.UP);

        assertEquals(new Position(2, 0), player.getPosition());
        assertEquals(0, this.gamesWon);
    }

    /**
     * Ensure that the player can escape if there is no data on the board.
     */
    @Test
    void canEscape() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        board.attach(this);
        board.setTile(2, 0, Exit.INSTANCE);
        Player player = new Player(board, 2, 1);
        board.addEntity(player);

        player.move(Direction.UP);

        assertEquals(new Position(2, 0), player.getPosition());
        assertEquals(1, this.gamesWon);
    }

    /**
     * Ensure the player can escape without collecting all bonus rewards.
     */
    @Test
    void canEscapeWithoutCollectingSourceCode() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        board.attach(this);
        board.setTile(2, 0, Exit.INSTANCE);
        Player player = new Player(board, 2, 1);
        board.addEntity(player);
        board.addEntity(new SourceCode(board, 2, 2, 1000));

        player.move(Direction.UP);

        assertEquals(new Position(2, 0), player.getPosition());
        assertEquals(1, this.gamesWon);
    }

    /**
     * Ensure that the player can exit once they collect all data,
     * after failing to exit before.
     */
    @Test
    void canEscapeAfterCollectingData() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        board.attach(this);
        board.setTile(2, 0, Exit.INSTANCE);
        Player player = new Player(board, 2, 1);
        board.addEntity(player);
        board.addEntity(new Data(board, 2, 2));

        player.move(Direction.UP);
        assertEquals(new Position(2, 0), player.getPosition());
        assertEquals(0, this.gamesWon);

        player.move(Direction.DOWN);
        player.move(Direction.DOWN);
        player.move(Direction.UP);
        player.move(Direction.UP);

        assertEquals(new Position(2, 0), player.getPosition());
        assertEquals(1, this.gamesWon);
    }


    /**
     * Ensure that the antivirus cannot exit the board.
     */
    @Test
    void antivirusCannotExit() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        board.attach(this);
        board.setTile(2, 0, Exit.INSTANCE);
        Antivirus antivirus = new Antivirus(board, 2, 1);
        board.addEntity(antivirus);

        antivirus.move(Direction.UP);
        assertEquals(new Position(2, 0), antivirus.getPosition());
        assertEquals(0, this.gamesWon);
    }

    @Test
    void loadCorrectAsset() {
        Board board = TestHelper.createEnclosedBoard(5, 5);

        assertSame(ResourceManager.loadSprite("exit_left.png"), imageAt(board, new Position(0, 2)));
        assertSame(ResourceManager.loadSprite("exit_right.png"), imageAt(board, new Position(4, 2)));
        assertSame(ResourceManager.loadSprite("exit_up.png"), imageAt(board, new Position(2, 0)));
        assertSame(ResourceManager.loadSprite("exit_down.png"), imageAt(board, new Position(2, 4)));
    }

    @Test
    void loadFallbackToLeftAsset() {
        Board board = TestHelper.createEnclosedBoard(5, 5);

        assertSame(ResourceManager.loadSprite("exit_left.png"), imageAt(board, new Position(2, 2)));
    }

    private static Image imageAt(Board board, Position position) {
        ImageView node = (ImageView) Exit.INSTANCE.createNode(board, position);
        return node.getImage();
    }

    @Override
    public void onWin(int dataCollected) {
        BoardObserver.super.onWin(dataCollected);
        this.gamesWon++;
    }
}

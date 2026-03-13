package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.HackingGame;
import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.BoardObserver;
import ca.sfu.cmpt276.group15.board.entity.Entity;
import ca.sfu.cmpt276.group15.board.entity.Player;
import ca.sfu.cmpt276.group15.math.Direction;
import ca.sfu.cmpt276.group15.math.Position;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;

public class GameMenu extends Menu implements BoardObserver {
    private final Board board;

    public GameMenu(HackingGame game, Board board) {
        super(game);
        this.board = board;

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Node node = board.getTile(x, y).createNode(board, new Position(x, y));
                node.setTranslateX(Position.fromGrid(x));
                node.setTranslateY(Position.fromGrid(y));
                node.setTranslateZ(-1.0);
                this.getChildren().add(node);
            }
        }

        board.getEntities().forEach(this::onEntityAdded);
        board.attach(this);
    }

    @Override
    public void onOpen() {
        super.onOpen();
        this.board.start();
    }

    @Override
    public void onClose() {
        super.onClose();
        this.board.close();
    }

    @Override
    public void onKeyPressed(KeyEvent event) {
        Entity entity = this.board.getFirstEntityMatching(e -> e instanceof Player);
        if (entity instanceof Player player) {
            switch (event.getCode()) {
                case W -> player.startMoving(Direction.UP);
                case A -> player.startMoving(Direction.LEFT);
                case S -> player.startMoving(Direction.DOWN);
                case D -> player.startMoving(Direction.RIGHT);
            }
        }
    }

    @Override
    public void onKeyReleased(KeyEvent event) {
        Entity entity = this.board.getFirstEntityMatching(e -> e instanceof Player);
        if (entity instanceof Player player) {
            switch (event.getCode()) {
                case W -> player.stopMoving(Direction.UP);
                case A -> player.stopMoving(Direction.LEFT);
                case S -> player.stopMoving(Direction.DOWN);
                case D -> player.stopMoving(Direction.RIGHT);
            }
        }
    }

    @Override
    public void onEntityAdded(Entity entity) {
        Platform.runLater(() -> this.getChildren().add(entity.getRenderNode()));
    }

    @Override
    public void onEntityRemoved(Entity entity) {
        Platform.runLater(() -> this.getChildren().remove(entity.getRenderNode()));
    }

    @Override
    public void onWin(int dataCollected) {
        Platform.runLater(() -> this.game.openMenu(new WinMenu(game, dataCollected)));
    }

    @Override
    public void onLose() {
        Platform.runLater(() -> this.game.openMenu(new GameOverMenu(game)));
    }
}

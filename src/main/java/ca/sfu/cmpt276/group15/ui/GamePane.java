package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.HackingGame;
import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.entity.Entity;
import ca.sfu.cmpt276.group15.board.entity.Player;
import ca.sfu.cmpt276.group15.math.Direction;
import ca.sfu.cmpt276.group15.math.Position;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class GamePane extends Pane {
    private final HackingGame game;
    private final Board board;

    public GamePane(HackingGame game, Board board) {
        this.game = game;
        this.board = board;

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Node render = board.getTile(x, y).render(board, new Position(x, y));
                render.setTranslateX(Position.fromGrid(x));
                render.setTranslateY(Position.fromGrid(y));
                this.getChildren().add(render);
            }
        }
        for (Entity entity : board.getEntities()) {
            Node node = entity.initRender();
            this.getChildren().add(node);
        }
    }

    public void start() {
        this.getScene().setOnKeyPressed(this::onKeyPressed);
        this.getScene().setOnKeyReleased(this::onKeyReleased);

        board.start();
    }

    private void onKeyReleased(KeyEvent keyEvent) {
        //todo actual movement logic.
        switch (keyEvent.getCode()) {
            case W -> {
                for (Entity entity : board.getEntities()) {
                    if (entity instanceof Player player) player.move(Direction.UP);
                }
            }
            case A -> {
                for (Entity entity : board.getEntities()) {
                    if (entity instanceof Player player) player.move(Direction.LEFT);
                }
            }
            case S -> {
                for (Entity entity : board.getEntities()) {
                    if (entity instanceof Player player) {
                        player.move(Direction.DOWN);
                        System.out.println("DOWN");
                    }
                }}
            case D -> {
                for (Entity entity : board.getEntities()) {
                    if (entity instanceof Player player) player.move(Direction.RIGHT);
                }
            }
        }
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case W -> {}
            case A -> {}
            case S -> {}
            case D -> {}
        }
    }
}

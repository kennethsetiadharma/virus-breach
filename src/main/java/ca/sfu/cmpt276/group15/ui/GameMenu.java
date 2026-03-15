package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.HackingGame;
import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.BoardObserver;
import ca.sfu.cmpt276.group15.board.entity.Data;
import ca.sfu.cmpt276.group15.board.entity.Entity;
import ca.sfu.cmpt276.group15.board.entity.Player;
import ca.sfu.cmpt276.group15.math.Direction;
import ca.sfu.cmpt276.group15.math.Position;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameMenu extends Menu implements BoardObserver {
    private final Board board;
    private PauseMenu pauseOverlay = null;
    private final Hud hud;
    private int dataCollectedCount = 0;

    private final Group camera = new Group();
    private final Group entities = new Group();
    private final javafx.scene.transform.Scale cameraScale = new javafx.scene.transform.Scale(1, 1, 0, 0);

    private final Rectangle viewportClip = new Rectangle();

    // use animationtimer so it updates every fps
    private final AnimationTimer cameraTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            updateCamera();
            hud.update(board, dataCollectedCount);
        }
    };

    public GameMenu(HackingGame game, Board board) {
        super(game);
        this.board = board;
        this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        this.entities.getTransforms().add(this.cameraScale);

        this.viewportClip.widthProperty().bind(this.widthProperty());
        this.viewportClip.heightProperty().bind(this.heightProperty());
        this.setClip(this.viewportClip);
        this.camera.getChildren().add(this.entities);
        this.getChildren().add(this.camera);

        for (int x = 0; x < board.width(); x++) {
            for (int y = 0; y < board.height(); y++) {
                Node node = board.getTile(x, y).createNode(board, new Position(x, y));
                node.setTranslateX(Position.fromGrid(x));
                node.setTranslateY(Position.fromGrid(y));
                node.setTranslateZ(-1.0);
                this.entities.getChildren().add(node);
            }
        }

        board.getEntities().forEach(this::onEntityAdded);
        board.attach(this);

        this.hud = new Hud(this::togglePause);
        this.hud.prefWidthProperty().bind(this.widthProperty());
        this.hud.prefHeightProperty().bind(this.heightProperty());
        this.getChildren().add(this.hud);
    }

    @Override
    public void onOpen() {
        super.onOpen();
        this.cameraTimer.start();
        this.board.start();
        Platform.runLater(this::updateCamera);
    }

    @Override
    public void onClose() {
        super.onClose();
        this.cameraTimer.stop();
        this.board.close();
    }

    @Override
    public void onKeyPressed(KeyEvent event) {
        if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
            togglePause();
            return;
        }
        if (pauseOverlay != null) return; // block movement while paused
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

    private void togglePause() {
        if (pauseOverlay == null) {
            pauseOverlay = new PauseMenu(game, this::togglePause);
            pauseOverlay.prefWidthProperty().bind(this.widthProperty());
            pauseOverlay.prefHeightProperty().bind(this.heightProperty());
            this.getChildren().add(pauseOverlay);
            this.board.setPaused(true);
        } else {
            this.getChildren().remove(pauseOverlay);
            pauseOverlay = null;
            this.board.setPaused(false);
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
        Platform.runLater(() -> {
            this.entities.getChildren().add(entity.getRenderNode());
            this.updateCamera();
        });
    }

    @Override
    public void onEntityRemoved(Entity entity) {
        if (entity instanceof Data) {
            this.dataCollectedCount++;
        }
        Platform.runLater(() -> {
            this.entities.getChildren().remove(entity.getRenderNode());
            this.updateCamera();
        });
    }

    @Override
    public void onWin(int dataCollected) {
        AudioManager.play("success.wav");
        Platform.runLater(() -> this.game.openMenu(new WinMenu(game, dataCollected)));
    }

    @Override
    public void onLose() {
        Platform.runLater(() -> this.game.openMenu(new GameOverMenu(game)));
    }

    private void updateCamera() {
        double boardPixelWidth = Position.fromGrid(this.board.width());
        double boardPixelHeight = Position.fromGrid(this.board.height());

        // Scale to fit the entire board within the window, preserving aspect ratio
        double zoom = Math.min(this.getWidth() / boardPixelWidth, this.getHeight() / boardPixelHeight);
        this.cameraScale.setX(zoom);
        this.cameraScale.setY(zoom);

        // Center the board in the window
        this.camera.setTranslateX((this.getWidth() - boardPixelWidth * zoom) / 2.0);
        this.camera.setTranslateY((this.getHeight() - boardPixelHeight * zoom) / 2.0);
    }
}

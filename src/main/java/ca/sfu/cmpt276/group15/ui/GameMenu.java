package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.HackingGame;
import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.BoardObserver;
import ca.sfu.cmpt276.group15.board.entity.Entity;
import ca.sfu.cmpt276.group15.board.entity.Player;
import ca.sfu.cmpt276.group15.math.Direction;
import ca.sfu.cmpt276.group15.math.Position;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;

public class GameMenu extends Menu implements BoardObserver {
    private final Board board;
    private PauseMenu pauseOverlay = null;

    private final Group camera = new Group();
    private final Group entities = new Group();
    
    private final Rectangle viewportClip = new Rectangle();

    // use animationtimer so it updates every fps
    private final AnimationTimer cameraTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            updateCamera();
        }
    };

    public GameMenu(HackingGame game, Board board) {
        super(game);
        this.board = board;
        this.entities.getTransforms().add(new Scale(HackingGame.CAMERA_ZOOM, HackingGame.CAMERA_ZOOM, 0, 0));

        this.viewportClip.widthProperty().bind(this.widthProperty());
        this.viewportClip.heightProperty().bind(this.heightProperty());
        this.setClip(this.viewportClip);
        this.camera.getChildren().add(this.entities);
        this.getChildren().add(this.camera);

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Node node = board.getTile(x, y).createNode(board, new Position(x, y));
                node.setTranslateX(Position.fromGrid(x));
                node.setTranslateY(Position.fromGrid(y));
                node.setTranslateZ(-1.0);
                this.entities.getChildren().add(node);
            }
        }

        board.getEntities().forEach(this::onEntityAdded);
        board.attach(this);
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
        Entity<?> entity = this.board.getFirstEntityMatching(e -> e instanceof Player);
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
        } else {
            this.getChildren().remove(pauseOverlay);
            pauseOverlay = null;
        }
    }

    @Override
    public void onKeyReleased(KeyEvent event) {
        Entity<?> entity = this.board.getFirstEntityMatching(e -> e instanceof Player);
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
    public void onEntityAdded(Entity<?> entity) {
        Platform.runLater(() -> {
            this.entities.getChildren().add(entity.getRenderNode());
            this.updateCamera();
        });
    }

    @Override
    public void onEntityRemoved(Entity<?> entity) {
        Platform.runLater(() -> {
            this.entities.getChildren().remove(entity.getRenderNode());
            this.updateCamera();
        });
    }

    @Override
    public void onWin(int dataCollected) {
        Platform.runLater(() -> this.game.openMenu(new WinMenu(game, dataCollected)));
    }

    @Override
    public void onLose() {
        Platform.runLater(() -> this.game.openMenu(new GameOverMenu(game)));
    }

    private void updateCamera() {
        Entity<?> entity = this.board.getFirstEntityMatching(e -> e instanceof Player);
        if (!(entity instanceof Player player)) {
            return;
        }

        double boardWidth = Position.fromGrid(this.board.getWidth());
        double boardHeight = Position.fromGrid(this.board.getHeight());

        double zoomedBoardWidth = boardWidth * HackingGame.CAMERA_ZOOM;
        double zoomedBoardHeight = boardHeight * HackingGame.CAMERA_ZOOM;

        double playerCentreX = Position.fromGrid(player.getPosition().x()) + Position.UNIT_SIZE / 2.0;
        double playerCentreY = Position.fromGrid(player.getPosition().y()) + Position.UNIT_SIZE / 2.0;

        double translateX = this.getWidth() / 2.0 - playerCentreX * HackingGame.CAMERA_ZOOM;
        double translateY = this.getHeight() / 2.0 - playerCentreY * HackingGame.CAMERA_ZOOM;

        translateX = restrictToViewport(translateX, zoomedBoardWidth, this.getWidth());
        translateY = restrictToViewport(translateY, zoomedBoardHeight, this.getHeight());

        this.camera.setTranslateX(translateX);
        this.camera.setTranslateY(translateY);
    }

    private static double restrictToViewport(double translate, double contentSize, double viewportSize) {
        if (contentSize <= viewportSize) {
            return (viewportSize - contentSize) / 2.0;
        }

        double minTranslate = viewportSize - contentSize;
        return Math.max(minTranslate, Math.min(0.0, translate));
    }
}

package ca.sfu.cmpt276.group15.ui.menu;

import ca.sfu.cmpt276.group15.HackingGame;
import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.BoardObserver;
import ca.sfu.cmpt276.group15.board.tile.OpenDoor;
import ca.sfu.cmpt276.group15.board.tile.TileType;
import ca.sfu.cmpt276.group15.board.entity.Data;
import ca.sfu.cmpt276.group15.board.entity.FreezeToken;
import ca.sfu.cmpt276.group15.board.entity.Entity;
import ca.sfu.cmpt276.group15.board.entity.Player;
import ca.sfu.cmpt276.group15.math.Direction;
import ca.sfu.cmpt276.group15.math.Position;
import ca.sfu.cmpt276.group15.ui.AudioManager;
import ca.sfu.cmpt276.group15.ui.Hud;
import ca.sfu.cmpt276.group15.ui.TutorialOverlay;
import javafx.animation.FadeTransition;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.util.Duration;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;

/**
 * Main game menu that displays the board and entities.
 * Also handles player input, pauses, and tutorial display.
 * Implements {@link BoardObserver} to update the view when the board changes, and to handle win/lose.
 */
public class GameMenu extends Menu implements BoardObserver {
    private final Board board;
    private PauseMenu pauseOverlay = null;
    private final Hud hud;
    private final Rectangle damageFlash;
    private int dataCollectedCount = 0;

    private final Group camera = new Group();
    private final Group entities = new Group();
    private Node[][] tileNodes;

    /**
     * Initializes the game menu with the given game and board.
     * Sets up the viewport, camera, and the HUD
     * 
     * @param game the game instance from {@link HackingGame}
     * @param board the board instance, see {@link Board}
     */
    public GameMenu(HackingGame game, Board board) {
        super(game);
        this.board = board;
        this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        this.entities.getTransforms().add(new Scale(HackingGame.CAMERA_ZOOM, HackingGame.CAMERA_ZOOM, 0, 0));

        Rectangle viewportClip = new Rectangle();
        viewportClip.widthProperty().bind(this.widthProperty());
        viewportClip.heightProperty().bind(this.heightProperty());
        this.setClip(viewportClip);
        this.camera.getChildren().add(this.entities);
        this.getChildren().add(this.camera);

        this.tileNodes = new Node[board.height()][board.width()];
        for (int x = 0; x < board.width(); x++) {
            for (int y = 0; y < board.height(); y++) {
                Node node = board.getTile(x, y).createNode(board, new Position(x, y));
                node.setTranslateX(Position.fromGrid(x));
                node.setTranslateY(Position.fromGrid(y));
                node.setTranslateZ(-1.0);
                this.tileNodes[y][x] = node;
                this.entities.getChildren().add(node);
            }
        }

        board.getEntities().forEach(this::onEntityAdded);
        board.attach(this);

        this.damageFlash = new Rectangle();
        this.damageFlash.widthProperty().bind(this.widthProperty());
        this.damageFlash.heightProperty().bind(this.heightProperty());
        this.damageFlash.setFill(Color.RED);
        this.damageFlash.setOpacity(0);
        this.damageFlash.setMouseTransparent(true);

        this.hud = new Hud(this::togglePause, this::flashDamage);
        this.hud.prefWidthProperty().bind(this.widthProperty());
        this.hud.prefHeightProperty().bind(this.heightProperty());
        this.getChildren().addAll(this.damageFlash, this.hud);
    }

    @Override
    public void onOpen() {
        super.onOpen();
        this.board.start();
        this.board.setPaused(true);
        Platform.runLater(this::showTutorial);
    }

    /**
     * Shows the tutorial overlay on top of the game view, and pauses the game
     */
    private void showTutorial() {
        TutorialOverlay[] ref = new TutorialOverlay[1];
        ref[0] = new TutorialOverlay(() -> {
            this.getChildren().remove(ref[0]);
            AudioManager.play("click.wav");
            this.board.setPaused(false);
        });
        ref[0].prefWidthProperty().bind(this.widthProperty());
        ref[0].prefHeightProperty().bind(this.heightProperty());
        this.getChildren().add(ref[0]);
    }

    @Override
    public void onClose() {
        super.onClose();
        this.board.close();
    }

    /**
     * Handles keydown events for player movement and pause
     *
     * @param event the key event
     */
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

    /**
     * Toggles the pause state of the game
     * Shows the pause menu, and pauses the board
     */
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

    /**
     * Handles keyup events to stop player from moving
     *
     * @param event the key event
     */
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

    /**
     * Called when an entity is added to the board
     * Adds the render node to the view and updates the camera
     * 
     * @param entity the entity that was added
     */
    @Override
    public void onEntityAdded(Entity entity) {
        Platform.runLater(() -> this.entities.getChildren().add(entity.getRenderNode()));
    }

    /**
     * Called when an entity is removed from the board
     * Removes the entity's render node from the view and updates the camera
     *
     * @param entity the entity that was removed
     */
    @Override
    public void onEntityRemoved(Entity entity) {
        if (entity instanceof Data) {
            this.dataCollectedCount++;
        }
        if (entity instanceof FreezeToken) {
            Platform.runLater(() -> hud.showFreezeBanner());
        }
        Platform.runLater(() -> this.entities.getChildren().remove(entity.getRenderNode()));
    }

    /**
     * Called when a tile on the board changes.
     * Replaces the old tile node with a new one at the same position.
     *
     * @param x    the x-coordinate of the changed tile
     * @param y    the y-coordinate of the changed tile
     * @param tile the new tile type
     */
    @Override
    public void onTileChanged(int x, int y, TileType tile) {
        if (tile == OpenDoor.INSTANCE) {
            Platform.runLater(() -> hud.showServerRoomBanner());
        }
        Platform.runLater(() -> {
            Node oldNode = this.tileNodes[y][x];
            Node newNode = tile.createNode(this.board, new Position(x, y));
            newNode.setTranslateX(Position.fromGrid(x));
            newNode.setTranslateY(Position.fromGrid(y));
            newNode.setTranslateZ(-1.0);
            this.tileNodes[y][x] = newNode;
            int index = this.entities.getChildren().indexOf(oldNode);
            this.entities.getChildren().set(index, newNode);
        });
    }

    /**
     * Called when the player wins the game
     * Plays a success sound and opens the win menu
     *
     * @param dataCollected the amount of data collected
     */
    @Override
    public void onWin(int dataCollected) {
        AudioManager.play("success.wav");
        int timePlayed = board.getTimePlayed();
        Platform.runLater(() -> this.game.openMenu(new WinMenu(game, dataCollected, timePlayed)));
    }

    /**
     * Called when the player loses the game
     * Opens the game over menu
     */
    @Override
    public void onLose() {
        int timePlayed = board.getTimePlayed();
        Platform.runLater(() -> this.game.openMenu(new GameOverMenu(game, timePlayed)));
    }

    @Override
    public void onUpdate() {
        BoardObserver.super.onUpdate();
        Platform.runLater(() -> {
            Player player = null;
            for (Entity entity : this.board.getEntities()) {
                if (entity instanceof Player p) player = p;
                entity.syncToView();
            }
            if (player != null) this.updateCamera(player);
            this.hud.update(board, dataCollectedCount);
        });
    }

    /**
     * Updates the camera to follow the player, clamped to the board edges.
     */
    private void updateCamera(Player player) {
        double boardWidth  = Position.fromGrid(this.board.width());
        double boardHeight = Position.fromGrid(this.board.height());

        double zoomedBoardWidth  = boardWidth  * HackingGame.CAMERA_ZOOM;
        double zoomedBoardHeight = boardHeight * HackingGame.CAMERA_ZOOM;

        double playerCentreX = Position.fromGrid(player.getPosition().x()) + Position.UNIT_SIZE / 2.0;
        double playerCentreY = Position.fromGrid(player.getPosition().y()) + Position.UNIT_SIZE / 2.0;

        double translateX = this.getWidth()  / 2.0 - playerCentreX * HackingGame.CAMERA_ZOOM;
        double translateY = this.getHeight() / 2.0 - playerCentreY * HackingGame.CAMERA_ZOOM;

        translateX = restrictToViewport(translateX, zoomedBoardWidth,  this.getWidth());
        translateY = restrictToViewport(translateY, zoomedBoardHeight, this.getHeight());

        this.camera.setTranslateX(translateX);
        this.camera.setTranslateY(translateY);
    }

    private void flashDamage() {
        damageFlash.setOpacity(0.35);
        FadeTransition fade = new FadeTransition(Duration.millis(500), damageFlash);
        fade.setFromValue(0.35);
        fade.setToValue(0);
        fade.play();
    }

    private static double restrictToViewport(double translate, double contentSize, double viewportSize) {
        if (contentSize <= viewportSize) {
            return (viewportSize - contentSize) / 2.0;
        }
        double minTranslate = viewportSize - contentSize;
        return Math.max(minTranslate, Math.min(0.0, translate));
    }
}

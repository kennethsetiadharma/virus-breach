package ca.sfu.cmpt276.virusbreach.ui.menu;

import ca.sfu.cmpt276.virusbreach.VirusBreach;
import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.board.BoardObserver;
import ca.sfu.cmpt276.virusbreach.board.entity.Data;
import ca.sfu.cmpt276.virusbreach.board.entity.Entity;
import ca.sfu.cmpt276.virusbreach.board.entity.FreezeToken;
import ca.sfu.cmpt276.virusbreach.board.entity.Player;
import ca.sfu.cmpt276.virusbreach.board.tile.TileType;
import ca.sfu.cmpt276.virusbreach.board.tile.TileTypes;
import ca.sfu.cmpt276.virusbreach.math.Direction;
import ca.sfu.cmpt276.virusbreach.math.Position;
import ca.sfu.cmpt276.virusbreach.ui.*;
import ca.sfu.cmpt276.virusbreach.ui.node.RenderNode;
import ca.sfu.cmpt276.virusbreach.ui.node.RenderNodeRegistry;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

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
    private final Node[][] tileNodes;

    /**
     * Initializes the game menu with the given game and board.
     * Sets up the viewport, camera, and the HUD
     * 
     * @param game the game instance from {@link VirusBreach}
     * @param board the board instance, see {@link Board}
     */
    public GameMenu(VirusBreach game, Board board) {
        super(game);
        this.board = board;
        this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        this.entities.getTransforms().add(new Scale(VirusBreach.CAMERA_ZOOM, VirusBreach.CAMERA_ZOOM, 0, 0));

        Rectangle viewportClip = new Rectangle();
        viewportClip.widthProperty().bind(this.widthProperty());
        viewportClip.heightProperty().bind(this.heightProperty());
        this.setClip(viewportClip);
        this.camera.getChildren().add(this.entities);
        this.getChildren().add(this.camera);

        this.tileNodes = new Node[board.height()][board.width()];
        for (int x = 0; x < board.width(); x++) {
            for (int y = 0; y < board.height(); y++) {
                Node node = RenderNodeRegistry.createRenderNodeForTile(board, new Position(x, y));
                this.tileNodes[y][x] = node;
                this.entities.getChildren().add(node);
            }
        }

        board.iterateEntities(this::onEntityAdded);
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
        AudioManager.playMusic("game_music.wav");
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
        this.board.stop();
    }

    /**
     * Handles keydown events for player movement and pause
     *
     * @param event the key event
     */
    @Override
    public void onKeyPressed(KeyEvent event) {
        super.onKeyPressed(event);
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
            AudioManager.pauseMusic();
        } else {
            this.getChildren().remove(pauseOverlay);
            pauseOverlay = null;
            this.board.setPaused(false);
            AudioManager.resumeMusic();
        }
    }

    /**
     * Handles keyup events to stop player from moving
     *
     * @param event the key event
     */
    @Override
    public void onKeyReleased(KeyEvent event) {
        super.onKeyReleased(event);
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
        runImmediatelyIfOnFx(() -> this.entities.getChildren().add(RenderNodeRegistry.createRenderNodeFor(entity)));
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
            runImmediatelyIfOnFx(() -> this.dataCollectedCount++);
        }
        if (entity instanceof FreezeToken) {
            runImmediatelyIfOnFx(hud::showFreezeBanner);
        }
        runImmediatelyIfOnFx(() -> this.entities.getChildren().removeIf(n -> n instanceof RenderNode<?> rn && rn.getObject() == entity));
    }

    /**
     * Called when a tile on the board changes.
     * Replaces the old tile node with a new one at the same position.
     *
     * @param position the position of the changed tile
     * @param tile the new tile type
     */
    @Override
    public void onTileChanged(Position position, TileType tile) {
        if (tile == TileTypes.OPEN_DOOR) {
            Platform.runLater(hud::showServerRoomBanner);
        }
        Platform.runLater(() -> {
            Node oldNode = this.tileNodes[position.y()][position.x()];
            Node newNode = RenderNodeRegistry.createRenderNodeForTile(this.board, position);
            this.tileNodes[position.y()][position.x()] = newNode;
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
        AudioManager.stopMusic();
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
        AudioManager.stopMusic();
        int timePlayed = board.getTimePlayed();
        Platform.runLater(() -> this.game.openMenu(new GameOverMenu(game, timePlayed)));
    }

    @Override
    public void onUpdate() {
        BoardObserver.super.onUpdate();
        Platform.runLater(() -> {
            for (Node child : this.entities.getChildren()) {
                if (child instanceof RenderNode<?> node) {
                    node.synchronize();
                }
            }
            Entity entity = this.board.getFirstEntityMatching(e -> e instanceof Player);
            if (entity instanceof Player player) this.updateCamera(player);
            this.hud.update(board, dataCollectedCount);
        });
    }

    /**
     * Updates the camera to follow the player, clamped to the board edges.
     */
    private void updateCamera(Player player) {
        double boardWidth  = Position.fromGrid(this.board.width());
        double boardHeight = Position.fromGrid(this.board.height());

        double zoomedBoardWidth  = boardWidth  * VirusBreach.CAMERA_ZOOM;
        double zoomedBoardHeight = boardHeight * VirusBreach.CAMERA_ZOOM;

        double playerCentreX = Position.fromGrid(player.getPosition().x()) + Position.UNIT_SIZE / 2.0;
        double playerCentreY = Position.fromGrid(player.getPosition().y()) + Position.UNIT_SIZE / 2.0;

        double translateX = this.getWidth()  / 2.0 - playerCentreX * VirusBreach.CAMERA_ZOOM;
        double translateY = this.getHeight() / 2.0 - playerCentreY * VirusBreach.CAMERA_ZOOM;

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

    /**
     * Getter for camera node.
     * 
     * @return the camera node
     */
    Group getCameraNode() {
        return this.camera;
    }

    /**
     * Getter for damage flash node.
     * 
     * @return the damage flash node
     */
    Rectangle getDamageFlashNode() {
        return this.damageFlash;
    }

    /**
     * Getter for a tile node at given x,y.
     * 
     * @param position the position of the tile
     * @return the tile node
     */
    Node getTileNode(Position position) {
        return this.tileNodes[position.y()][position.x()];
    }

    private static double restrictToViewport(double translate, double contentSize, double viewportSize) {
        if (contentSize <= viewportSize) {
            return (viewportSize - contentSize) / 2.0;
        }
        double minTranslate = viewportSize - contentSize;
        return Math.clamp(translate, minTranslate, 0.0);
    }

    private static void runImmediatelyIfOnFx(Runnable todo) {
        if (Platform.isFxApplicationThread()) {
            todo.run();
        } else {
            Platform.runLater(todo);
        }
    }
}

package ca.sfu.cmpt276.virusbreach;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.board.tile.TileType;
import ca.sfu.cmpt276.virusbreach.ui.AudioManager;
import ca.sfu.cmpt276.virusbreach.ui.menu.GameMenu;
import ca.sfu.cmpt276.virusbreach.ui.menu.Menu;
import ca.sfu.cmpt276.virusbreach.ui.menu.TitleMenu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Initializes the game window and manages menu navigation
 */
public class VirusBreach extends Application {
    /**
     * How often to run an update on the board, in milliseconds.
     */
    public static final long UPDATE_INTERVAL = 100;
    /**
     * The width of the board to generate, in tiles.
     */
    public static final int BOARD_WIDTH = 46;
    /**
     * The height of the board to generate, in tiles.
     */
    public static final int BOARD_HEIGHT = 26;
    /**
     * How much to scale the viewport by.
     */
    public static final double CAMERA_ZOOM = 4.0;

    private final GameOptions options = new GameOptions();
    /**
     * The root window used for display and input.
     */
    Stage stage;
    /**
     * The currently opened menu.
     * Should never be {@code null} once the game starts.
     */
    Menu activeMenu;

    /**
     * Creates a new game instance.
     *
     * @see Application#launch(Class, String...)
     */
    public VirusBreach() {
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        AudioManager.init(this.options);
        this.stage.setWidth(1280);
        this.stage.setHeight(720);
        this.stage.setResizable(false);
        this.stage.setTitle("Virus Breach");

        this.activeMenu = new TitleMenu(this);
        this.stage.setScene(new Scene(this.activeMenu));
        this.activeMenu.onOpen();

        this.stage.show();
    }

    @Override
    public void stop() {
        this.activeMenu.onClose();
    }

    /**
     * {@return the configured options for this game}
     */
    public GameOptions getOptions() {
        return options;
    }

    /**
     * Starts a new game, generating a board and opening the main game screen.
     */
    public void startNewGame() {
        Board board = new Board(new TileType[BOARD_HEIGHT][BOARD_WIDTH]);
        BoardGenerator.generateBoard(board);
        this.openMenu(new GameMenu(this, board));
    }

    /**
     * Opens the specified menu for display, after closing the presently active one.
     *
     * @param menu the menu to be opened
     */
    public void openMenu(Menu menu) {
        this.activeMenu.onClose();
        this.activeMenu = menu;
        this.stage.getScene().setRoot(menu);
        this.activeMenu.onOpen();
    }
}

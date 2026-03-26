package ca.sfu.cmpt276.group15;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.tile.TileType;
import ca.sfu.cmpt276.group15.ui.AudioManager;
import ca.sfu.cmpt276.group15.ui.menu.GameMenu;
import ca.sfu.cmpt276.group15.ui.menu.Menu;
import ca.sfu.cmpt276.group15.ui.menu.TitleMenu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main class for the game.
 * Initializes the game window and manages menu navigation
 */
public class HackingGame extends Application {
    public static final long UPDATE_INTERVAL = 100; //milliseconds
    public static final int BOARD_WIDTH = 46;
    public static final int BOARD_HEIGHT = 26;
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
        this.stage.setOnCloseRequest(e -> this.close());
    }

    public static void main(String[] args) {
        launch(args);
    }

    public HackingGame() {
    }

    public GameOptions getOptions() {
        return options;
    }

    /**
     * Called when the game window is being closed (shutdown).
     */
    void close() {
        this.activeMenu.onClose();
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

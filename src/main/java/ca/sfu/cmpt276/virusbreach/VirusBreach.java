package ca.sfu.cmpt276.virusbreach;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.board.BoardGenerator;
import ca.sfu.cmpt276.virusbreach.board.tile.TileType;
import ca.sfu.cmpt276.virusbreach.ui.AudioManager;
import ca.sfu.cmpt276.virusbreach.ui.menu.GameMenu;
import ca.sfu.cmpt276.virusbreach.ui.menu.Menu;
import ca.sfu.cmpt276.virusbreach.ui.menu.TitleMenu;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

/**
 * Initializes the game window and manages menu navigation
 */
public class VirusBreach extends Application {
    /**
     * Game's base width in pixels before scaling
     */
    public static final double BASE_WIDTH = 1280.0;
    /**
     * Game's base height in pixels before scaling
     */
    public static final double BASE_HEIGHT = 720.0;
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
     * Fixed size root that contains the active menu before scaling.
     */
    private Pane contentRoot;

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
        this.stage.setWidth(BASE_WIDTH);
        this.stage.setHeight(BASE_HEIGHT);
        this.stage.setResizable(true);
        this.stage.setTitle("Virus Breach");

        this.stage.setScene(createScaledScene());
        this.activeMenu = new TitleMenu(this);
        setActiveMenuRoot(this.activeMenu);
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
     * {@return the current active menu}
     */
    public Menu getActiveMenu() {
        return activeMenu;
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
        setActiveMenuRoot(menu);
        this.activeMenu.onOpen();
    }

    /**
     * Sets a menu as the only child of contentRoot
     * 
     * @param menu the menu to set as active root
     */
    private void setActiveMenuRoot(Menu menu) {
        menu.setMinSize(BASE_WIDTH, BASE_HEIGHT);
        menu.setPrefSize(BASE_WIDTH, BASE_HEIGHT);
        menu.setMaxSize(BASE_WIDTH, BASE_HEIGHT);
        menu.resize(BASE_WIDTH, BASE_HEIGHT);
        this.contentRoot.getChildren().setAll(menu);
    }

    /**
     * Creates scene with fixed size contentRoot scaled to fit the javafx window.
     * 
     * @return the created scene
     */
    private Scene createScaledScene() {
        Pane viewport = new Pane();
        viewport.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        this.contentRoot = new Pane();
        this.contentRoot.setMinSize(BASE_WIDTH, BASE_HEIGHT);
        this.contentRoot.setPrefSize(BASE_WIDTH, BASE_HEIGHT);
        this.contentRoot.setMaxSize(BASE_WIDTH, BASE_HEIGHT);
        this.contentRoot.resize(BASE_WIDTH, BASE_HEIGHT);

        Group scaledContent = new Group(this.contentRoot);
        Scale contentScale = new Scale(1.0, 1.0, 0.0, 0.0);
        scaledContent.getTransforms().add(contentScale);

        viewport.getChildren().add(scaledContent);

        Scene scene = new Scene(viewport, BASE_WIDTH, BASE_HEIGHT, Color.BLACK);

        NumberBinding scale = Bindings.min(
                scene.widthProperty().divide(BASE_WIDTH),
                scene.heightProperty().divide(BASE_HEIGHT)
        );
        contentScale.xProperty().bind(scale);
        contentScale.yProperty().bind(scale);

        scaledContent.translateXProperty().bind(scene.widthProperty().subtract(Bindings.multiply(BASE_WIDTH, scale)).divide(2));
        scaledContent.translateYProperty().bind(scene.heightProperty().subtract(Bindings.multiply(BASE_HEIGHT, scale)).divide(2));
        
        return scene;
    }
}

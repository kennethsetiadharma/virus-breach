package ca.sfu.cmpt276.group15;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.tile.TileType;
import ca.sfu.cmpt276.group15.ui.GameMenu;
import ca.sfu.cmpt276.group15.ui.Menu;
import ca.sfu.cmpt276.group15.ui.TitleMenu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class HackingGame extends Application {
    public static final long UPDATE_INTERVAL = 100; //milliseconds
    public static final int BOARD_WIDTH = 32;
    public static final int BOARD_HEIGHT = 32;
    public static final double CAMERA_ZOOM = 4.0;

    private final GameOptions options = new GameOptions();
    private Stage stage;
    private Menu activeMenu;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.stage.setWidth(1280);
        this.stage.setHeight(720);
        this.stage.setResizable(false);
        this.stage.setTitle("Hacking Game");

        this.activeMenu = new TitleMenu(this);
        this.stage.setScene(new Scene(this.activeMenu));
        this.activeMenu.onOpen();

        this.stage.show();
        this.stage.setOnCloseRequest(this::close);
    }

    public static void main(String[] args) {
        launch();
    }

    public HackingGame() {
    }

    public GameOptions getOptions() {
        return options;
    }

//    @Override
//    protected void initGame() {
//        int viewportBoundWidth = Math.max((int)Position.fromGrid(BOARD_WIDTH), getAppWidth());
//        int viewportBoundHeight = Math.max((int)Position.fromGrid(BOARD_HEIGHT), getAppHeight());
//
//        getGameScene().getViewport().setBounds(0, 0, viewportBoundWidth, viewportBoundHeight);
//        // getGameScene().getViewport().setLazy(true);
//        getGameScene().getViewport().setZoom(CAMERA_ZOOM);
//        getGameScene().setBackgroundColor(Color.DARKGRAY);
//        getGameWorld().addEntityFactory(new HackingGameEntityFactory());
//
//        BoardGenerator.generateBoard(getGameWorld(), BOARD_WIDTH, BOARD_HEIGHT);
//        var player = getGameScene().getGameWorld().spawn("player", getGameWorld().getSingleton(EntityType.ENTRANCE).getPosition());
//        getGameScene().getViewport().bindToEntity(
//            player,
//            (getAppWidth() - Position.UNIT_SIZE) / 2.0,
//            (getAppHeight() - Position.UNIT_SIZE) / 2.0
//        );
//
//        getGameTimer().runAtInterval(this::update, UPDATE_INTERVAL);
//    }

    private void close(WindowEvent event) {
        this.activeMenu.onClose();
    }

    public void startNewGame() {
        Board board = new Board(new TileType[BOARD_HEIGHT][BOARD_WIDTH]);
        BoardGenerator.generateBoard(board);
        this.openMenu(new GameMenu(this, board));
    }

    public void openMenu(Menu menu) {
        this.activeMenu.onClose();
        this.activeMenu = menu;
        this.stage.getScene().setRoot(menu);
        this.activeMenu.onOpen();
    }
}

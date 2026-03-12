package ca.sfu.cmpt276.group15;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.tile.TileType;
import ca.sfu.cmpt276.group15.ui.GamePane;
import ca.sfu.cmpt276.group15.ui.TitleMenu;
import ca.sfu.cmpt276.group15.ui.WinMenu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HackingGame extends Application {
    public static final long UPDATE_INTERVAL = 100; //milliseconds
    public static final int BOARD_WIDTH = 32;
    public static final int BOARD_HEIGHT = 32;
    public static final double CAMERA_ZOOM = 4.0;

    private final GameOptions options = new GameOptions();
    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setWidth(1280);
        stage.setHeight(720);
        stage.setTitle("Hacking Game");
        stage.setScene(new Scene(new TitleMenu(this)));
        stage.show();
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

    public void startNewGame() {
        Board board = new Board(new TileType[BOARD_HEIGHT][BOARD_WIDTH]);
        BoardGenerator.generateBoard(board);
        //todo gen
        Scene scene = this.stage.getScene();
//        scene.setCamera(new ParallelCamera());
        GamePane value = new GamePane(this, board);
        scene.setRoot(value);
        value.start();
    }
}

package ca.sfu.cmpt201.group15.render.screen;

import ca.sfu.cmpt201.group15.board.Board;

public class GameScreen extends Screen {
    private final Board board;

    public GameScreen(Screen parent, Board board) {
        super(parent);
        this.board = board;
    }

    @Override
    public boolean onClick(double mouseX, double mouseY) {
        return true;
    }

    @Override
    public boolean onKeyPress(int keyCode) {
        return true;
    }

    @Override
    public void render(double mouseX, double mouseY) {

    }
}

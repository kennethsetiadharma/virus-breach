package ca.sfu.cmpt276.group15;

import ca.sfu.cmpt276.group15.board.Board;

public class GameThread extends Thread {
    private final Board board;

    public GameThread(Board board) {
        this.board = board;
    }

    @Override
    public void run() {
        super.run();
        while (true) {
//            this.board.tick();

        }

    }
}

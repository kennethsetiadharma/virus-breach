package ca.sfu.cmpt276.group15.board;

import ca.sfu.cmpt276.group15.BoardGenerator;
import ca.sfu.cmpt276.group15.board.entity.FreezeToken;
import ca.sfu.cmpt276.group15.math.Position;

public class ServerRoom extends Room {
    public ServerRoom(int x, int y, int width, int height, Position entrance) {
        super(x, y, width, height, entrance);
    }

    @Override
    public boolean furnishRoom(Board board) {
        generateInternalLayout(board);
        spawnEntities(board);
        return true;
    }

    @Override
    protected void spawnEntities(Board board) {
        BoardGenerator.spawnAnywhere(board, this.x + 1, this.y + 1,
                this.x + this.width, this.y + this.height, FreezeToken::new);
    }

    @Override
    protected void generateInternalLayout(Board board) {
        // No internal walls for now
    }
}

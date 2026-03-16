package ca.sfu.cmpt276.group15.board;

import ca.sfu.cmpt276.group15.board.entity.DecryptionKey;
import ca.sfu.cmpt276.group15.board.tile.Wall;
import ca.sfu.cmpt276.group15.math.Position;

public class StorageRoom extends Room {
    private final Position serverRoomDoor;

    public StorageRoom(int x, int y, int width, int height, Position entrance, Position serverRoomDoor) {
        super(x, y, width, height, entrance);
        this.serverRoomDoor = serverRoomDoor;
    }

    @Override
    public boolean furnishRoom(Board board) {
        generateInternalLayout(board);
        spawnEntities(board);
        return true;
    }

    @Override
    protected void spawnEntities(Board board) {
        board.addEntity(new DecryptionKey(board, this.x + this.width - 1, this.y + 1, serverRoomDoor));
    }

    /**
     * Places a cross-shaped wall cluster centered in the Storage Room interior.
     * The cross gives the player cover to dodge the Antivirus enemy.
     */
    @Override
    protected void generateInternalLayout(Board board) {
        int cx = this.x + this.width / 2;  // horizontal centre of room interior
        int cy = this.y + this.height / 2; // vertical centre of room interior

        // Horizontal arm (length 2 on each side)
        for (int dx = -2; dx <= 2; dx++) {
            board.setTile(cx + dx, cy, Wall.INSTANCE);
        }
        // Vertical arm (length 2 above and below, centre already placed)
        for (int dy = -2; dy <= 2; dy++) {
            board.setTile(cx, cy + dy, Wall.INSTANCE);
        }
    }
}

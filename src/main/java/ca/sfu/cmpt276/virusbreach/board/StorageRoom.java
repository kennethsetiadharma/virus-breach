package ca.sfu.cmpt276.virusbreach.board;

import ca.sfu.cmpt276.virusbreach.board.entity.DecryptionKey;
import ca.sfu.cmpt276.virusbreach.board.tile.Wall;
import ca.sfu.cmpt276.virusbreach.math.Position;

/**
 * The Storage Room, located in the bottom-right corner of the board.
 * Contains a cross-shaped internal wall layout and the {@link ca.sfu.cmpt276.virusbreach.board.entity.DecryptionKey},
 * which unlocks the Server Room door when collected.
 */
public class StorageRoom extends Room {
    private final Position serverRoomDoor;

    /**
     * @param x              the x-coordinate of the room's left wall
     * @param y              the y-coordinate of the room's top wall
     * @param width          the room width in tiles
     * @param height         the room height in tiles
     * @param entrance       the position of the room's doorway
     * @param serverRoomDoor the tile position of the Server Room door to unlock via the Decryption Key
     */
    public StorageRoom(int x, int y, int width, int height, Position entrance, Position serverRoomDoor) {
        super(x, y, width, height, entrance);
        this.serverRoomDoor = serverRoomDoor;
    }

    /**
     * Generates the internal layout then spawns entities inside the room.
     *
     * @param board the board to furnish
     * @return {@code true} when furnishing completes successfully
     */
    @Override
    public boolean furnishRoom(Board board) {
        generateInternalLayout(board);
        spawnEntities(board);
        return true;
    }

    /**
     * Spawns the {@link DecryptionKey} in the top-right corner of the room interior.
     *
     * @param board the board to spawn entities on
     */
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

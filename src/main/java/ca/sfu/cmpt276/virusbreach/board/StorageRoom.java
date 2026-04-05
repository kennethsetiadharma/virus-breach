package ca.sfu.cmpt276.virusbreach.board;

import ca.sfu.cmpt276.virusbreach.board.entity.DecryptionKey;
import ca.sfu.cmpt276.virusbreach.board.tile.TileTypes;
import ca.sfu.cmpt276.virusbreach.math.Position;

/**
 * The Storage Room, located in the bottom-right corner of the board.
 * Contains a cross-shaped internal wall layout and the {@link ca.sfu.cmpt276.virusbreach.board.entity.DecryptionKey},
 * which unlocks the Server Room door when collected.
 */
public class StorageRoom extends Room {
    private final Position serverRoomDoor;

    /**
     * Creates a room that will generate a decryption key.
     *
     * @param position the position of the room's left wall
     * @param width          the room width in tiles
     * @param height         the room height in tiles
     * @param entrance       the position of the room's doorway
     * @param serverRoomDoor the tile position of the Server Room door to unlock via the Decryption Key
     */
    public StorageRoom(Position position, int width, int height, Position entrance, Position serverRoomDoor) {
        super(position, width, height, entrance);
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
    protected void spawnEntities(Board board) {
        board.addEntity(new DecryptionKey(board, new Position(this.x + this.width - 1, this.y + 1), serverRoomDoor));
    }

    /**
     * Places a cross-shaped wall cluster centered in the Storage Room interior.
     * The cross gives the player cover to dodge the Antivirus enemy.
     *
     * @param board the board to generate on
     */
    protected void generateInternalLayout(Board board) {
        int cx = this.x + this.width / 2;  // horizontal centre of room interior
        int cy = this.y + this.height / 2; // vertical centre of room interior

        // Horizontal arm (length 2 on each side)
        for (int dx = -2; dx <= 2; dx++) {
            board.setTile(new Position(cx + dx, cy), TileTypes.WALL);
        }
        // Vertical arm (length 2 above and below, centre already placed)
        for (int dy = -2; dy <= 2; dy++) {
            board.setTile(new Position(cx, cy + dy), TileTypes.WALL);
        }
    }
}

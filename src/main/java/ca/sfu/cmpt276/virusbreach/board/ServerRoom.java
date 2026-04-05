package ca.sfu.cmpt276.virusbreach.board;

import ca.sfu.cmpt276.virusbreach.BoardGenerator;
import ca.sfu.cmpt276.virusbreach.board.entity.FreezeToken;
import ca.sfu.cmpt276.virusbreach.board.tile.TileTypes;
import ca.sfu.cmpt276.virusbreach.math.Position;

/**
 * The Server Room, located in the top-left corner of the board.
 * Access is locked behind the {@link TileTypes#LOCKED_DOOR Locked Door}.
 * until the player collects the {@link ca.sfu.cmpt276.virusbreach.board.entity.DecryptionKey Decryption Key}
 * from the Storage Room. Contains the {@link ca.sfu.cmpt276.virusbreach.board.entity.FreezeToken Freeze Token} buff.
 */
public class ServerRoom extends Room {
    /**
     * Creates a room that will generate a freeze token buff.
     *
     * @param position the position of the room's left wall
     * @param width    the room width in tiles
     * @param height   the room height in tiles
     * @param entrance the position of the room's doorway
     */
    public ServerRoom(Position position, int width, int height, Position entrance) {
        super(position, width, height, entrance);
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
     * Spawns the {@link FreezeToken} at a random free tile inside the room interior.
     *
     * @param board the board to spawn entities on
     */
    protected void spawnEntities(Board board) {
        BoardGenerator.spawnAnywhere(board, this.x + 1, this.y + 1,
                this.x + this.width, this.y + this.height, FreezeToken::new);
    }

    /**
     * Places an L-shaped wall in each corner of the Server Room interior,
     * each arm matching the direction of its corner.
     *
     * @param board the board to generate on
     */
    protected void generateInternalLayout(Board board) {
        int left   = this.x + 2;
        int right  = this.x + this.width - 2;
        int top    = this.y + 2;
        int bottom = this.y + this.height - 2;
        int arm    = 3; // tiles per arm including the corner tile

        // Top-left: arms go right and down
        for (int i = 0; i < arm; i++) board.setTile(new Position(left + i, top),     TileTypes.WALL);
        for (int i = 1; i < arm; i++) board.setTile(new Position(left,     top + i), TileTypes.WALL);

        // Top-right: arms go left and down
        for (int i = 0; i < arm; i++) board.setTile(new Position(right - i, top),     TileTypes.WALL);
        for (int i = 1; i < arm; i++) board.setTile(new Position(right,     top + i), TileTypes.WALL);

        // Bottom-left: arms go right and up
        for (int i = 0; i < arm; i++) board.setTile(new Position(left + i, bottom),     TileTypes.WALL);
        for (int i = 1; i < arm; i++) board.setTile(new Position(left,     bottom - i), TileTypes.WALL);

        // Bottom-right: arms go left and up
        for (int i = 0; i < arm; i++) board.setTile(new Position(right - i, bottom),     TileTypes.WALL);
        for (int i = 1; i < arm; i++) board.setTile(new Position(right,     bottom - i), TileTypes.WALL);

        // Small cross centered in the room (arm length 1)
        int cx = this.x + this.width / 2;
        int cy = this.y + this.height / 2;
        for (int dx = -1; dx <= 1; dx++) board.setTile(new Position(cx + dx, cy),      TileTypes.WALL);
        for (int dy = -1; dy <= 1; dy++) board.setTile(new Position(cx,      cy + dy), TileTypes.WALL);
    }
}

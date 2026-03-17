package ca.sfu.cmpt276.group15.board;

import ca.sfu.cmpt276.group15.BoardGenerator;
import ca.sfu.cmpt276.group15.board.entity.FreezeToken;
import ca.sfu.cmpt276.group15.board.tile.Wall;
import ca.sfu.cmpt276.group15.math.Position;

/**
 * The Server Room, located in the top-left corner of the board.
 * Access is locked behind the {@link ca.sfu.cmpt276.group15.board.tile.LockedDoor}
 * until the player collects the {@link ca.sfu.cmpt276.group15.board.entity.DecryptionKey}
 * from the Storage Room. Contains the {@link ca.sfu.cmpt276.group15.board.entity.FreezeToken} buff.
 */
public class ServerRoom extends Room {
    /**
     * @param x        the x-coordinate of the room's left wall
     * @param y        the y-coordinate of the room's top wall
     * @param width    the room width in tiles
     * @param height   the room height in tiles
     * @param entrance the position of the room's doorway
     */
    public ServerRoom(int x, int y, int width, int height, Position entrance) {
        super(x, y, width, height, entrance);
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
    @Override
    protected void spawnEntities(Board board) {
        BoardGenerator.spawnAnywhere(board, this.x + 1, this.y + 1,
                this.x + this.width, this.y + this.height, FreezeToken::new);
    }

    /**
     * Places an L-shaped wall in each corner of the Server Room interior,
     * each arm matching the direction of its corner.
     */
    @Override
    protected void generateInternalLayout(Board board) {
        int left   = this.x + 2;
        int right  = this.x + this.width - 2;
        int top    = this.y + 2;
        int bottom = this.y + this.height - 2;
        int arm    = 3; // tiles per arm including the corner tile

        // Top-left: arms go right and down
        for (int i = 0; i < arm; i++) board.setTile(left + i, top,     Wall.INSTANCE);
        for (int i = 1; i < arm; i++) board.setTile(left,     top + i, Wall.INSTANCE);

        // Top-right: arms go left and down
        for (int i = 0; i < arm; i++) board.setTile(right - i, top,     Wall.INSTANCE);
        for (int i = 1; i < arm; i++) board.setTile(right,     top + i, Wall.INSTANCE);

        // Bottom-left: arms go right and up
        for (int i = 0; i < arm; i++) board.setTile(left + i, bottom,     Wall.INSTANCE);
        for (int i = 1; i < arm; i++) board.setTile(left,     bottom - i, Wall.INSTANCE);

        // Bottom-right: arms go left and up
        for (int i = 0; i < arm; i++) board.setTile(right - i, bottom,     Wall.INSTANCE);
        for (int i = 1; i < arm; i++) board.setTile(right,     bottom - i, Wall.INSTANCE);

        // Small cross centered in the room (arm length 1)
        int cx = this.x + this.width / 2;
        int cy = this.y + this.height / 2;
        for (int dx = -1; dx <= 1; dx++) board.setTile(cx + dx, cy,      Wall.INSTANCE);
        for (int dy = -1; dy <= 1; dy++) board.setTile(cx,      cy + dy, Wall.INSTANCE);
    }
}

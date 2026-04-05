package ca.sfu.cmpt276.virusbreach.board.tile;

/**
 * Contains all the tile singleton instances.
 */
public final class TileTypes {
    /**
     * Singleton for the player's spawning/starting tile.
     * There should only be one per board.
     */
    public static final TileType ENTRANCE = new TileType(false);

    /**
     * The escape tile for the player.
     * If the player has collected all data, then they can win the game by stepping on this tile.
     */
    public static final TileType EXIT = new Exit();

    /**
     * Represents a walkable tile. Most tiles in the game will be floor.
     * Has no special properties.
     */
    public static final TileType FLOOR = new TileType(false);
    /**
     * A locked door tile that blocks movement.
     * Replaced by {@link #OPEN_DOOR} when the player collects the Decryption Key.
     */
    public static final TileType LOCKED_DOOR = new TileType(true);

    /**
     * A tile type that prevents movement.
     * Divides rooms and encloses the board.
     */
    public static final TileType WALL = new TileType(true);
    /**
     * An open door tile that allows movement through it.
     * Replaces {@link #LOCKED_DOOR} when the player collects the Decryption Key.
     */
    public static final TileType OPEN_DOOR = new TileType(false);

    TileTypes() {
        throw new UnsupportedOperationException("Class cannot be instantiated");
    }
}

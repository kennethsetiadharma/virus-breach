package ca.sfu.cmpt276.group15.board.tile;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.entity.Entity;
import ca.sfu.cmpt276.group15.math.Position;
import javafx.scene.Node;

/**
 * Represents the floor and obstacles on the board.
 * Subclasses generally follow the singleton pattern.
 *
 * @see Board
 */
public abstract class TileType {
    /**
     * If {@code true}, then the tile cannot be traversed onto
     */
    private final boolean solid;

    public TileType(boolean solid) {
        this.solid = solid;
    }

    /**
     * Called when an entity steps on a tile of this type.
     *
     * @param board    the board that the tile is placed on
     * @param position the position of the tile on the board
     * @param entity   the entity that is stepping on the tile
     */
    public void onStep(Board board, Position position, Entity entity) {
    }

    /**
     * Called when an entity stops stepping on a tile of this type.
     *
     * @param board    the board that the tile is placed on
     * @param position the position of the tile on the board
     * @param entity   the entity that is leaving the tile
     */
    public void onLeave(Board board, Position position, Entity entity) {
    }

    public boolean isSolid() {
        return this.solid;
    }

    /**
     * Creates a render node for display.
     *
     * @param board    the board that the tile is placed on
     * @param position the position of the tile on the board
     * @return a {@link Node} that represents this tile, for display.
     */
    public abstract Node createNode(Board board, Position position);
}

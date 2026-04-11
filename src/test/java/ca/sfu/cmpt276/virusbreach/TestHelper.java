package ca.sfu.cmpt276.virusbreach;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.board.tile.TileType;
import ca.sfu.cmpt276.virusbreach.board.tile.TileTypes;

import java.util.Arrays;

public final class TestHelper {
    /**
     * Creates a board with the specified dimensions, with walls on all sides.
     *
     * @param width the horizontal length of the board, in tiles
     * @param height the vertical length of the board, in tiles
     * @return a newly created board
     */
    public static Board createEnclosedBoard(int width, int height) {
        TileType[][] tiles = new TileType[height][width];

        Arrays.fill(tiles[0], TileTypes.WALL);
        for (int h = 1; h < height - 1; h++) {
            Arrays.fill(tiles[h], TileTypes.FLOOR);
            tiles[h][0] = TileTypes.WALL;
            tiles[h][width - 1] = TileTypes.WALL;
        }
        Arrays.fill(tiles[height - 1], TileTypes.WALL);

        return new Board(tiles);
    }
}

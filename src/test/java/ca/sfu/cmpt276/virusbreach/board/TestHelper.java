package ca.sfu.cmpt276.virusbreach.board;

import ca.sfu.cmpt276.virusbreach.board.tile.TileType;
import ca.sfu.cmpt276.virusbreach.board.tile.TileTypes;

import java.util.Arrays;

public class TestHelper {
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

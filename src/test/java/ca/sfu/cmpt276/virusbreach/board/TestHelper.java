package ca.sfu.cmpt276.virusbreach.board;

import ca.sfu.cmpt276.virusbreach.board.tile.Floor;
import ca.sfu.cmpt276.virusbreach.board.tile.TileType;
import ca.sfu.cmpt276.virusbreach.board.tile.Wall;

import java.util.Arrays;

public class TestHelper {
    public static Board createEnclosedBoard(int width, int height) {
        TileType[][] tiles = new TileType[height][width];

        Arrays.fill(tiles[0], Wall.INSTANCE);
        for (int h = 1; h < height - 1; h++) {
            Arrays.fill(tiles[h], Floor.INSTANCE);
            tiles[h][0] = Wall.INSTANCE;
            tiles[h][width - 1] = Wall.INSTANCE;
        }
        Arrays.fill(tiles[height - 1], Wall.INSTANCE);

        return new Board(tiles);
    }
}

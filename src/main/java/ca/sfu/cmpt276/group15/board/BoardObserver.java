package ca.sfu.cmpt276.group15.board;

import ca.sfu.cmpt276.group15.board.entity.Entity;
import ca.sfu.cmpt276.group15.board.tile.TileType;

public interface BoardObserver {
    default void onEntityAdded(Entity entity) {}
    default void onEntityRemoved(Entity entity) {}

    default void onWin(int dataCollected) {}
    default void onLose() {}

    default void onTileChanged(int x, int y, TileType tile) {}
}

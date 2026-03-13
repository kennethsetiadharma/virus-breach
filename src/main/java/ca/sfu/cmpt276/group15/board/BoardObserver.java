package ca.sfu.cmpt276.group15.board;

import ca.sfu.cmpt276.group15.board.entity.Entity;

public interface BoardObserver {
    void onEntityAdded(Entity entity);
    void onEntityRemoved(Entity entity);

    void onWin(int dataCollected);
    void onLose();
}

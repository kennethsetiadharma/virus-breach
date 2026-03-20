package ca.sfu.cmpt276.group15.board;

import ca.sfu.cmpt276.group15.board.entity.Data;
import ca.sfu.cmpt276.group15.board.entity.Entity;
import ca.sfu.cmpt276.group15.board.tile.Floor;
import ca.sfu.cmpt276.group15.board.tile.TileType;
import ca.sfu.cmpt276.group15.board.tile.Wall;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardObserverTest {
    private Board board;
    private CountingObserver observer;

    @BeforeEach
    void setUp() {
        TileType[][] tiles = new TileType[][]{{Floor.INSTANCE}};
        this.board = new Board(tiles);
        this.observer = new BoardObserverTest.CountingObserver();
        this.board.attach(this.observer);
    }

    @AfterEach
    void tearDown() {
        this.board.detach(this.observer);
        this.board.close();
    }

    @Test
    void notifySetTile() {
        this.board.setTile(0, 0, Wall.INSTANCE);

        assertEquals(1, this.observer.tilesChanged);
    }

    @Test
    void notifyEntityChanges() {
        Data entity = new Data(this.board, 0, 0);
        this.board.addEntity(entity);

        assertEquals(0, this.observer.entitiesAdded);

        this.board.tick();

        assertEquals(1, this.observer.entitiesAdded);
        assertEquals(0, this.observer.entitiesRemoved);

        this.board.removeEntity(entity);

        assertEquals(0, this.observer.entitiesRemoved);

        this.board.tick();

        assertEquals(1, this.observer.entitiesAdded);
        assertEquals(1, this.observer.entitiesRemoved);
        assertEquals(2, this.observer.boardUpdates);
    }

    @Test
    void notifyUpdate() {
        this.board.tick();

        assertEquals(1, this.observer.boardUpdates);
    }

    @Test
    void notifyUpdatePause() {
        this.board.setPaused(true);

        this.board.tick();

        assertEquals(0, this.observer.boardUpdates);

        this.board.setPaused(false);

        this.board.tick();

        assertEquals(1, this.observer.boardUpdates);
    }

    @Test
    void notifyGameWon() {
        this.board.win(1000);

        assertEquals(1, this.observer.gamesWon);
        assertEquals(1000, this.observer.lastWinScore);
    }

    @Test
    void notifyGameLost() {
        this.board.lose();

        assertEquals(1, this.observer.gamesLost);
    }

    private static class CountingObserver implements BoardObserver {
        private int entitiesAdded = 0;
        private int entitiesRemoved = 0;
        private int gamesWon = 0;
        private int lastWinScore;
        private int gamesLost = 0;
        private int boardUpdates = 0;
        private int tilesChanged = 0;

        @Override
        public void onEntityAdded(Entity entity) {
            this.entitiesAdded++;
        }

        @Override
        public void onEntityRemoved(Entity entity) {
            this.entitiesRemoved++;
        }

        @Override
        public void onWin(int dataCollected) {
            this.gamesWon++;
            this.lastWinScore = dataCollected;
        }

        @Override
        public void onLose() {
            this.gamesLost++;
        }

        @Override
        public void onUpdate() {
            this.boardUpdates++;
        }

        @Override
        public void onTileChanged(int x, int y, TileType tile) {
            this.tilesChanged++;
        }
    }
}

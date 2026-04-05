package ca.sfu.cmpt276.virusbreach.board;

import ca.sfu.cmpt276.virusbreach.VirusBreach;
import ca.sfu.cmpt276.virusbreach.board.entity.Data;
import ca.sfu.cmpt276.virusbreach.board.entity.Entity;
import ca.sfu.cmpt276.virusbreach.board.entity.SourceCode;
import ca.sfu.cmpt276.virusbreach.board.tile.TileType;
import ca.sfu.cmpt276.virusbreach.board.tile.TileTypes;
import ca.sfu.cmpt276.virusbreach.math.Position;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest implements BoardObserver {
    private int removedEntites = 0;
    private int boardUpdates = 0;

    /**
     * Ensure that source code randomly spawns in the game.
     */
    @Test
    void randomlySpawnSourceCode() {
        Board board = TestHelper.createEnclosedBoard(5, 5);

        int ticks = 10000;
        do {
            board.tick();
        } while (--ticks >= 0 && board.iterateEntitiesYield(e -> e instanceof SourceCode ? true : null) == null);

        assertNotEquals(-1, ticks, "no source code spawned within 10000 ticks!");
    }

    /**
     * Ensure that randomly spawned source code expiries within a reasonable amount of time.
     */
    @Test
    void randomlySpawnedSourceCodeExpires() {
        Board board = TestHelper.createEnclosedBoard(5, 5);

        int ticks = 10000;
        SourceCode target;
        do {
            board.tick();
            target = board.iterateEntitiesYield(e -> e instanceof SourceCode sc ? sc : null);
        } while (--ticks > 0 && target == null);

        assertNotNull(target, "no source code spawned within 10000 ticks!");

        ticks = 150;
        do {
            board.tick();
        } while (--ticks > 0 && !target.isRemoved());

        assertTrue(target.isRemoved(), "source code did not expire within 150 ticks!");
    }

    /**
     * Ensure that source code only spawns in bounds and not on solid objects.
     */
    @Test
    void randomlySpawnSourceCodeInBounds() {
        Board board = TestHelper.createEnclosedBoard(5, 5);

        for (int i = 0; i < 10000; i++) {
            board.tick();
            board.iterateEntities(e -> {
                if (e instanceof SourceCode) {
                    assertTrue(board.contains(e.getPosition()));
                    assertFalse(board.getTile(e.getPosition()).isSolid());
                }
            });
        }
    }

    /**
     * Ensure that source code does not spawn on top of other entities.
     */
    @Test
    void noSourceCodeOverlaps() {
        Board board = TestHelper.createEnclosedBoard(3, 3);

        Data entity = new Data(board, new Position(1, 1));
        board.addEntity(entity);

        for (int i = 0; i < 10000; i++) {
            board.tick();
            assertEquals(1, board.getEntitiesAt(new Position(1, 1)).size());
            assertEquals(entity, board.getEntitiesAt(new Position(1, 1)).iterator().next());
        }
    }

    /**
     * Ensure that entities that have been removed cannot be re-added to the board.
     */
    @Test
    void cannotAddRemovedEntity() {
        Board board = TestHelper.createEnclosedBoard(3, 3);

        Data entity = new Data(board, new Position(1, 1));
        board.addEntity(entity);

        // mark entity as removed
        board.removeEntity(entity);

        board.addEntity(entity);
        assertEquals(0, board.getEntitiesAt(new Position(1, 1)).size());
    }

    /**
     * Ensure that callbacks for entities that have been removed wont be run multiple times.
     */
    @Test
    void cannotRemoveEntityMultipleTimes() {
        Board board = TestHelper.createEnclosedBoard(3, 3);
        board.attach(this);

        Data entity = new Data(board, new Position(1, 1));
        board.addEntity(entity);

        board.removeEntity(entity);
        board.removeEntity(entity);

        assertEquals(0, board.getEntitiesAt(new Position(1, 1)).size());
        assertEquals(1, this.removedEntites);
        board.detach(this);
    }

    /**
     * Empty boards cannot be created.
     */
    @Test
    void minimumBoardSize() {
        assertThrows(IllegalArgumentException.class, () -> new Board(new TileType[0][0]));
        assertThrows(IllegalArgumentException.class, () -> new Board(new TileType[1][0]));
        assertThrows(IllegalArgumentException.class, () -> new Board(new TileType[0][1]));
        assertDoesNotThrow(() -> new Board(new TileType[][] {{TileTypes.FLOOR}}));
    }

    /**
     * The board notifies listeners when it updates.
     */
    @Test
    void notifyUpdate() {
        Board board = TestHelper.createEnclosedBoard(3, 3);
        board.attach(this);
        board.tick();

        assertEquals(1, this.boardUpdates);
    }

    /**
     * The board does not tick when oaused.
     */
    @Test
    void notifyUpdatePause() {
        Board board = TestHelper.createEnclosedBoard(3, 3);
        board.attach(this);
        board.setPaused(true);

        board.tick();

        assertEquals(0, this.boardUpdates);
        assertEquals(0, board.getTimePlayed());

        board.setPaused(false);

        board.tick();

        assertEquals(1, this.boardUpdates);
        assertEquals(1, board.getTimePlayed());
    }

    /**
     * Ensure that the game can run on a separate thread with the update interval.
     */
    @Test
    void scheduledUpdate() {
        Board board = TestHelper.createEnclosedBoard(3, 3);
        try {
            board.attach(this);
            board.start();

            assertDoesNotThrow(() -> Thread.sleep(VirusBreach.UPDATE_INTERVAL + VirusBreach.UPDATE_INTERVAL / 2));

            assertNotEquals(0, this.boardUpdates);
        } finally {
            board.stop();
        }
    }

    /**
     * Test that exceptions are printed when raised in the board logic thread.
     */
    @Test
    void failException() {
        Board.testMode = true;
        Board board = TestHelper.createEnclosedBoard(3, 3);

        Entity entity = new Entity(board, new Position(1, 1)) {
            @Override
            public boolean isRemoved() {
                if (super.isRemoved()) throw new UnsupportedOperationException("Testing exception, ignore");
                return false;
            }
        };
        board.addEntity(entity);
        board.removeEntity(entity);

        PrintStream err = System.err;
        CountingStream stream = new CountingStream();
        try {
            System.setErr(new PrintStream(stream));
            assertThrowsExactly(RuntimeException.class, board::tick);
            assertNotEquals(0, stream.written);
        } finally {
            System.setErr(err);
        }
    }

    @Override
    public void onEntityRemoved(Entity entity) {
        this.removedEntites++;
    }

    @Override
    public void onUpdate() {
        this.boardUpdates++;
    }

    private static class CountingStream extends OutputStream {
        private int written;

        @Override
        public void write(int b) {
            this.written++;
        }
    }
}

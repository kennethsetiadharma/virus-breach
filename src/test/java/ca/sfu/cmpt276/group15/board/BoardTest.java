package ca.sfu.cmpt276.group15.board;

import ca.sfu.cmpt276.group15.board.entity.Data;
import ca.sfu.cmpt276.group15.board.entity.SourceCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
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

        Data entity = new Data(board, 1, 1);
        board.addEntity(entity);

        for (int i = 0; i < 10000; i++) {
            board.tick();
            assertEquals(1, board.getEntitiesAt(1, 1).size());
            assertEquals(entity, board.getEntitiesAt(1, 1).iterator().next());
        }
    }
}

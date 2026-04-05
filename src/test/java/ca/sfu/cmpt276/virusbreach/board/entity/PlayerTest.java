package ca.sfu.cmpt276.virusbreach.board.entity;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.board.TestHelper;
import ca.sfu.cmpt276.virusbreach.board.tile.TileType;
import ca.sfu.cmpt276.virusbreach.board.tile.TileTypes;
import ca.sfu.cmpt276.virusbreach.math.Direction;
import ca.sfu.cmpt276.virusbreach.math.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerTest {
    /**
     * Ensure the player can move upward.
     */
    @Test
    void moveUpward() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Player player = new Player(board, new Position(2, 2));
        board.addEntity(player);

        player.startMoving(Direction.UP);
        board.tick();

        assertEquals(new Position(2, 2).relative(Direction.UP), player.getPosition());
    }

    /**
     * Ensure the player can move downward.
     */
    @Test
    void moveDownward() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Player player = new Player(board, new Position(2, 2));
        board.addEntity(player);

        player.startMoving(Direction.DOWN);
        board.tick();

        assertEquals(new Position(2, 2).relative(Direction.DOWN), player.getPosition());
    }

    /**
     * Ensure the player can move leftward.
     */
    @Test
    void moveLeftward() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Player player = new Player(board, new Position(2, 2));
        board.addEntity(player);

        player.startMoving(Direction.LEFT);
        board.tick();

        assertEquals(new Position(2, 2).relative(Direction.LEFT), player.getPosition());
    }

    /**
     * Ensure the player can move rightward.
     */
    @Test
    void moveRightward() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Player player = new Player(board, new Position(2, 2));
        board.addEntity(player);

        player.startMoving(Direction.RIGHT);
        board.tick();

        assertEquals(new Position(2, 2).relative(Direction.RIGHT), player.getPosition());
    }

    /**
     * Ensure the player continues to move in the same direction (barring additional input).
     */
    @Test
    void movementRepeats() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Player player = new Player(board, new Position(1, 2));
        board.addEntity(player);

        player.startMoving(Direction.RIGHT);
        board.tick();
        board.tick();

        assertEquals(new Position(3, 2), player.getPosition());
    }

    /**
     * Ensure the player cannot move into walls.
     */
    @Test
    void cannotMoveIntoSolid() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Player player = new Player(board, new Position(2, 2));
        board.addEntity(player);
        board.setTile(new Position(1, 2), TileTypes.WALL);
        board.setTile(new Position(2, 3), TileTypes.WALL);
        board.setTile(new Position(3, 2), TileTypes.WALL);
        board.setTile(new Position(2, 1), TileTypes.WALL);

        for (Direction direction : Direction.values()) {
            player.startMoving(direction);
            board.tick();
            player.stopMoving(direction);

            assertEquals(new Position(2, 2), player.getPosition());
        }
    }

    /**
     * Ensure the player cannot move off the board.
     */
    @Test
    void cannotMoveOutOfBounds() {
        Board board = new Board(new TileType[][]{{TileTypes.FLOOR}});
        Player player = new Player(board, new Position(0, 0));
        board.addEntity(player);

        for (Direction direction : Direction.values()) {
            player.startMoving(direction);
            board.tick();
            player.stopMoving(direction);

            assertEquals(new Position(0, 0), player.getPosition());
        }
    }

    /**
     * Ensure the player prefers to move in the latest provided direction.
     */
    @Test
    void preferNewDirection() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Player player = new Player(board, new Position(2, 2));
        board.addEntity(player);

        player.startMoving(Direction.RIGHT);
        player.startMoving(Direction.UP);
        board.tick();

        assertEquals(new Position(2, 2).relative(Direction.UP), player.getPosition());
    }

    /**
     * Ensure the player can move in previously declared directions if it hits an obstacle.
     */
    @Test
    void preferNewDirectionFallback() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Player player = new Player(board, new Position(2, 2));
        board.addEntity(player);

        player.startMoving(Direction.RIGHT);
        player.startMoving(Direction.UP);
        board.tick();
        board.tick();

        assertEquals(new Position(2, 2).relative(Direction.UP).relative(Direction.RIGHT), player.getPosition());
    }

    /**
     * Ensure that missed key release events do not break the game.
     */
    @Test
    void repeatMovementIgnored() {
        Board board = TestHelper.createEnclosedBoard(5, 5);
        Player player = new Player(board, new Position(2, 2));
        board.addEntity(player);

        player.startMoving(Direction.RIGHT);
        player.startMoving(Direction.RIGHT);
        player.stopMoving(Direction.RIGHT);
        board.tick();

        assertEquals(new Position(2, 2), player.getPosition());
    }
}

package ca.sfu.cmpt276.virusbreach.board.entity;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.board.BoardObserver;
import ca.sfu.cmpt276.virusbreach.board.tile.TileType;
import ca.sfu.cmpt276.virusbreach.math.Direction;
import ca.sfu.cmpt276.virusbreach.math.Position;
import ca.sfu.cmpt276.virusbreach.ui.AudioManager;

import java.util.*;

/**
 * "Moving enemy" character that chases the player and instantly kills them on collision.
 */
public class Antivirus extends Entity implements BoardObserver {
    static final int MOVEMENT_TICKS = 2;

    private final boolean[][] solidState;
    private int movementCounter = MOVEMENT_TICKS;

    public Antivirus(Board board, Position position) {
        super(board, position);

        this.solidState = new boolean[this.board.height()][this.board.width()];
        for (int yi = 0; yi < this.solidState.length; yi++) {
            for (int xi = 0; xi < this.solidState[yi].length; xi++) {
                this.solidState[yi][xi] = this.board.getTile(new Position(xi, yi)).isSolid();
            }
        }
        this.board.attach(this);
    }

    /**
     * Get the current solid state 
     * 
     * @return boolean 2d array for solid state
     */
    boolean[][] getSolidState() {
        return this.solidState;
    }

    @Override
    public void onCollideWith(Entity entity) {
        super.onCollideWith(entity);
        if (entity instanceof Player player) {
            AudioManager.play("caught.wav");
            this.board.removeEntity(player);
            this.board.lose();
        }
    }

    /**
     * Called every game update cycle.
     * If there is a player on the board, the entity will move 1 step towards the player every {@link #MOVEMENT_TICKS} ticks.
     */
    @Override
    public void tick() {
        super.tick();

        if (--this.movementCounter == 0) {
            this.movementCounter = MOVEMENT_TICKS;

            Entity player = this.board.getFirstEntityMatching(e -> e instanceof Player);
            if (player != null) {
                Position target = player.getPosition();

                if (this.getPosition().equals(target)) return;

                Direction direction = pathfindTowards(target);
                if (direction != null) {
                    this.move(direction);
                }
            }
        }
    }

    /**
     * Pathfind towards the given position
     * Internally, it uses the <a href="https://en.wikipedia.org/wiki/A*_search_algorithm">A* search algorithm</a> to navigate to the point.
     *
     * @param target the position to move towards
     * @return the direction of the next step to move in to get closer to the target position
     */
    private Direction pathfindTowards(Position target) {
        Map<Position, Position> sources = new HashMap<>();
        Map<Position, Integer> cost = new HashMap<>();
        Map<Position, Integer> bestTo = new HashMap<>();
        PriorityQueue<Position> pending = new PriorityQueue<>(Comparator.comparing(p -> bestTo.getOrDefault(p, Integer.MAX_VALUE)));
        Set<Position> visited = new HashSet<>();

        pending.add(this.getPosition());
        cost.put(this.getPosition(), 0);
        bestTo.put(this.getPosition(), this.getPosition().manhattanDistance(target));

        Position current = null;
        while (!pending.isEmpty()) {
            current = pending.poll();
            visited.add(current);

            if (current.equals(target)) {
                break;
            }

            for (Direction direction : Direction.values()) {
                Position adj = current.relative(direction);
                if (!this.board.contains(adj) || this.solidState[adj.y()][adj.x()] || visited.contains(adj)) continue;
                int n = cost.get(current) + 1;
                if (n < cost.getOrDefault(adj, Integer.MAX_VALUE)) {
                    cost.put(adj, n);
                    bestTo.put(adj, n + adj.manhattanDistance(target));
                    sources.put(adj, current);
                    pending.add(adj);
                }
            }
        }

        if (target.equals(current)) {
            current = backtrackUntil(sources, current, this.getPosition());

            return Direction.fromVector(new Position(current.x() - this.getPosition().x(), current.y() - this.getPosition().y()));
        }
        return null;
    }

    /**
     * Follow the source map until the position right before the target.
     *
     * @param sources map from position to previous position to be followed
     * @param target the position to stop at
     * @param from the starting position
     * @return the position right before the target
     */
    private static Position backtrackUntil(Map<Position, Position> sources, Position target, Position from) {
        while (!sources.get(target).equals(from)) {
            target = sources.get(target);
        }
        return target;
    }

    @Override
    public void onTileChanged(Position position, TileType tile) {
        if (this.solidState[position.y()][position.x()] != this.board.getTile(position).isSolid()) {
            this.solidState[position.y()][position.x()] = !this.solidState[position.y()][position.x()];
        }
    }

    @Override
    public void onRemove() {
        super.onRemove();
        this.board.detach(this);
    }
}

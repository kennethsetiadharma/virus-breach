package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.BoardObserver;
import ca.sfu.cmpt276.group15.board.tile.TileType;
import ca.sfu.cmpt276.group15.math.Direction;
import ca.sfu.cmpt276.group15.math.Position;
import ca.sfu.cmpt276.group15.ui.AnimatedNode;
import ca.sfu.cmpt276.group15.ui.AudioManager;
import javafx.scene.Node;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * "Moving enemy" character that chases the player and instantly kills them on collision.
 */
public class Antivirus extends Entity implements BoardObserver {
    private static final int MOVEMENT_TICKS = 2;

    private final boolean[][] solidState;
    private int movementCounter = MOVEMENT_TICKS;
    private AnimatedNode node;

    public Antivirus(Board board, int x, int y) {
        super(board, x, y);

        this.solidState = new boolean[this.board.height()][this.board.width()];
        for (int yi = 0; yi < this.solidState.length; yi++) {
            for (int xi = 0; xi < this.solidState[yi].length; xi++) {
                this.solidState[yi][xi] = this.board.getTile(xi, yi).isSolid();
            }
        }
        this.board.attach(this);
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
     * Internally, it uses the {@link <a href="https://en.wikipedia.org/wiki/A*_search_algorithm">A* search algorithm</a>} to navigate to the player.
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
                    if (visited.contains(current)) continue;
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
                    while (!sources.get(current).equals(this.getPosition())) {
                        current = sources.get(current);
                    }

                    this.move(Direction.fromVector(current.x() - this.getPosition().x(), current.y() - this.getPosition().y()));
                }
            }
        }
    }

    @Override
    public void onTileChanged(int x, int y, TileType tile) {
        if (this.solidState[y][x] != this.board.getTile(x, y).isSolid()) {
            this.solidState[y][x] = !this.solidState[y][x];
        }
    }

    @Override
    public void onRemove() {
        super.onRemove();
        this.board.detach(this);
    }

    @Override
    protected Node createRenderNode() {
        return this.node = new AnimatedNode("antivirus.png", 4, "antivirus_moving1.png", "antivirus_moving2.png");
    }

    @Override
    public void syncToView() {
        super.syncToView();
        this.node.animate(this.getPosition(), this.getPrevPosition(), this.board.getTimePlayed());
    }
}

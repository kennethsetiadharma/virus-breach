package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.board.BoardObserver;
import ca.sfu.cmpt276.group15.board.tile.TileType;
import ca.sfu.cmpt276.group15.math.Direction;
import ca.sfu.cmpt276.group15.math.Position;
import ca.sfu.cmpt276.group15.ui.AudioManager;

import java.util.*;

public class Antivirus extends AnimatedEntity implements BoardObserver {
    private static final int MOVEMENT_TICKS = 2;
    private static final String[] MOVING_SPRITES = {
        "antivirus_moving1.png",
        "antivirus_moving2.png"
    };

    private final boolean[][] solidState;
    private int movementCounter = MOVEMENT_TICKS;

    public Antivirus(Board board, int x, int y) {
        super(board, x, y);

        this.solidState = new boolean[this.board.getHeight()][this.board.getWidth()];
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

                pending.add(this.getPosition());
                cost.put(this.getPosition(), 0);
                bestTo.put(this.getPosition(), this.getPosition().manhattanDistance(target));

                Position current = null;
                while (!pending.isEmpty()) {
                    current = pending.poll();
                    if (current.equals(target)) {
                        break;
                    }

                    for (Direction direction : Direction.values()) {
                        Position adj = current.relative(direction);
                        if (!this.board.contains(adj) || this.solidState[adj.y()][adj.x()]) continue;
                        int n = cost.get(current) + 1;
                        if (n < cost.getOrDefault(adj, Integer.MAX_VALUE)) {
                            cost.put(adj, n);
                            bestTo.put(adj, n + adj.manhattanDistance(target));
                            sources.put(adj, current);
                            if (!pending.contains(adj)) pending.add(adj);
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
    protected String getIdleSpriteAsset() {
        return "antivirus.png";
    }

    @Override
    protected String[] getMovingSpriteAssets() {
        return MOVING_SPRITES;
    }
}

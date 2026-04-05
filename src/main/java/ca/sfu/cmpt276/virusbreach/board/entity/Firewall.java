package ca.sfu.cmpt276.virusbreach.board.entity;

import ca.sfu.cmpt276.virusbreach.board.Board;
import ca.sfu.cmpt276.virusbreach.math.Direction;
import ca.sfu.cmpt276.virusbreach.math.Position;
import ca.sfu.cmpt276.virusbreach.ui.AudioManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Non-moving enemy that damages the player on colliding.
 */
public class Firewall extends Collectable {
    // Spread interval range in ticks (100ms each): 75–125 ticks = ~7.5–12.5 seconds
    private static final int MIN_SPREAD_TICKS = 75;
    private static final int MAX_SPREAD_TICKS = 125;

    private int spreadTimer;

    /**
     * Constructs a new firewall on the board at the given position.
     *
     * @param board the board to spawn on
     * @param position the location to spawn at
     */
    public Firewall(Board board, Position position) {
        super(board, position, -200);
        this.spreadTimer = board.getRandom().nextInt(MIN_SPREAD_TICKS, MAX_SPREAD_TICKS);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.board.getFreezeTimer() > 0) return;
        if (--this.spreadTimer <= 0) {
            this.spreadTimer = board.getRandom().nextInt(MIN_SPREAD_TICKS, MAX_SPREAD_TICKS);
            trySpread();
        }
    }

    /**
     * Attempts to spread to a random free adjacent tile.
     * Does nothing if all neighbours are occupied or solid.
     */
    private void trySpread() {
        List<Direction> dirs = new ArrayList<>(List.of(Direction.values()));
        Collections.shuffle(dirs, board.getRandom());

        for (Direction dir : dirs) {
            Position adj = this.getPosition().relative(dir);
            if (board.contains(adj) && !board.getTile(adj).isSolid() && board.getEntitiesAt(new Position(adj.x(), adj.y())).isEmpty()) {
                board.addEntity(new Firewall(board, new Position(adj.x(), adj.y())));
                return;
            }
        }
    }

    @Override
    public void onCollideWith(Entity entity) {
        if (entity instanceof Player) {
            AudioManager.play("damage.wav");
        }
        super.onCollideWith(entity);
    }
}

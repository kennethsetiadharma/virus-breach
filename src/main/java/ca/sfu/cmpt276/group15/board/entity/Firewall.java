package ca.sfu.cmpt276.group15.board.entity;

import ca.sfu.cmpt276.group15.board.Board;
import ca.sfu.cmpt276.group15.math.Direction;
import ca.sfu.cmpt276.group15.math.Position;
import ca.sfu.cmpt276.group15.ui.AudioManager;
import ca.sfu.cmpt276.group15.ui.ResourceManager;
import javafx.scene.Node;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Non moving enemy that damages the player on colliding.
 */
public class Firewall extends Collectable {
    // Spread interval range in ticks (100ms each): 75–125 ticks = ~7.5–12.5 seconds
    private static final int MIN_SPREAD_TICKS = 75;
    private static final int MAX_SPREAD_TICKS = 125;

    private static boolean frozen = false;

    /**
     * Freezes or unfreezes all firewalls on the board.
     * When frozen, firewalls stop spreading until this is set back to {@code false}.
     *
     * @param frozen {@code true} to freeze spreading, {@code false} to resume
     */
    public static void setFrozen(boolean frozen) {
        Firewall.frozen = frozen;
    }

    private int spreadTimer;

    public Firewall(Board board, int x, int y) {
        super(board, x, y, -200);
        this.spreadTimer = board.getRandom().nextInt(MIN_SPREAD_TICKS, MAX_SPREAD_TICKS);
    }

    @Override
    public void tick() {
        super.tick();
        if (frozen) return;
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
            if (board.contains(adj) && !board.getTile(adj).isSolid() && board.getEntitiesAt(adj.x(), adj.y()).isEmpty()) {
                board.addEntity(new Firewall(board, adj.x(), adj.y()));
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

    @Override
    public Node createRenderNode() {
        return ResourceManager.sprite("firewall.png", Color.ORANGE);
    }
}

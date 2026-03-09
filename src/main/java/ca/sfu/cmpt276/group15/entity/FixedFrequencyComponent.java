package ca.sfu.cmpt276.group15.entity;

import ca.sfu.cmpt276.group15.HackingGame;
import com.almasb.fxgl.entity.component.Component;

import ca.sfu.cmpt276.group15.math.Direction;
import ca.sfu.cmpt276.group15.math.Position;
import static ca.sfu.cmpt276.group15.math.Position.UNIT_SIZE;
import javafx.geometry.Point2D;

public abstract class FixedFrequencyComponent extends Component {
    public abstract void onUpdate();

    protected void move(Direction direction) {
        Point2D change = direction.asVector().multiply(UNIT_SIZE);

        double nextX = this.entity.getX() + change.getX();
        double nextY = this.entity.getY() + change.getY();
        Point2D nextPos = new Point2D(nextX, nextY);

        double maxX = Position.fromGrid(HackingGame.BOARD_WIDTH - 1);
        double maxY = Position.fromGrid(HackingGame.BOARD_HEIGHT - 1);

        if (nextX < 0 || nextX > maxX || nextY < 0 || nextY > maxY) {
            return;
        }

        boolean blocked = this.entity.getWorld().getEntitiesAt(nextPos).stream().anyMatch(e -> e != this.entity && e.isType(EntityType.WALL));

        if (blocked) {
            return;
        }

        this.entity.translate(change);
    }

    @Override
    public boolean isComponentInjectionRequired() {
        return false;
    }
}

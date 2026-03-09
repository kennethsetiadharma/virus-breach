package ca.sfu.cmpt276.group15.entity;

import ca.sfu.cmpt276.group15.math.Direction;
import ca.sfu.cmpt276.group15.math.Position;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarCell;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import com.almasb.fxgl.pathfinding.astar.AStarPathfinder;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AntivirusComponent extends FixedFrequencyComponent {
    private Iterator<Direction> path = null;
    private AStarGrid grid;
    private AStarPathfinder<AStarCell> pathfinder;
    private Point2D prevPlayerPos = null;

    public AntivirusComponent() {
    }

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
        if (this.grid == null) {
            //todo size
            this.grid = AStarGrid.fromWorld(this.entity.getWorld(), 128, 128, Position.UNIT_SIZE, Position.UNIT_SIZE, o -> CellState.WALKABLE);
            this.pathfinder = new AStarPathfinder<>(this.grid);
        }
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        this.grid = null;
        this.pathfinder = null;
    }

    @Override
    public void onUpdate() {
        if (this.grid == null) return;

        int x = Position.asGrid(this.entity.getX());
        int y = Position.asGrid(this.entity.getY());
        Point2D pos = this.entity.getWorld().getSingletonOptional(EntityType.PLAYER).map(Entity::getPosition).orElse(null);
        if (pos != null) {
            if (!pos.equals(this.prevPlayerPos)) {
                this.prevPlayerPos = pos;
                List<AStarCell> path = this.pathfinder.findPath(this.grid.getData(),
                    this.grid.get(x, y),
                    this.grid.get(Position.asGrid(pos.getX()), Position.asGrid(pos.getY())));
                List<Direction> directions = new ArrayList<>();
                for (AStarCell cell : path) {
                    directions.add(Direction.fromVector(cell.getX() - x, cell.getY() - y));
                    x = cell.getX();
                    y = cell.getY();
                }
                this.path = directions.iterator();
            }
        } else {
            this.path = null;
        }

        if (this.path != null && this.path.hasNext()) {
            this.move(this.path.next());
        }
    }
}

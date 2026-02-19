package ca.sfu.cmpt201.group15.math;

public record Position(int x, int y) implements Comparable<Position> {
    public Position relative(int x, int y) {
        return new Position(this.x + x, this.y + y);
    }

    public Position relative(Direction direction) {
        return this.relative(direction.getX(), direction.getY());
    }

    public Position up() {
        return this.relative(Direction.UP);
    }

    public Position down() {
        return this.relative(Direction.DOWN);
    }

    public Position left() {
        return this.relative(Direction.LEFT);
    }

    public Position right() {
        return this.relative(Direction.RIGHT);
    }

    @Override
    public int compareTo(Position position) {
        int x = Integer.compare(this.x, position.x);
        return x == 0 ? Integer.compare(this.y, position.y) : x;
    }
}

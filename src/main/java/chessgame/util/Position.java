package chessgame.util;

public final class Position {
    private final int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int distanceToInSquare(Position position) {
        return (int) (Math.pow(position.x - x, 2) + Math.pow(position.y - y, 2));
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", x, y);
    }
}

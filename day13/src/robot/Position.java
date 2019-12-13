package robot;

import java.util.Objects;

public class Position {
    public final int x;
    public final int y;

    public static Position xy(int x, int y) {
        return new Position(x, y);
    }

    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position step(Orientation direction) {
        return switch(direction) {
            case RIGHT -> xy(x+1, y);
            case DOWN  -> xy(x, y+1);
            case LEFT  -> xy(x-1, y);
            case UP    -> xy(x, y-1);
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position xy = (Position) o;
        return x == xy.x &&
                y == xy.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

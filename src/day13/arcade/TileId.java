package day13.arcade;

public enum TileId {
    EMPTY, WALL, BLOCK, HORIZONTAL_PADDLE, BALL;

    public static TileId fromInt(int n) {
        return switch(n) {
            case 0 -> EMPTY;
            case 1 -> WALL;
            case 2 -> BLOCK;
            case 3 -> HORIZONTAL_PADDLE;
            case 4 -> BALL;
            default -> throw new IllegalStateException("Should not happen");
        };
    }

    public String render() {
        return switch(this) {
            case EMPTY -> " ";
            case WALL -> "#";
            case BLOCK -> "·";
            case HORIZONTAL_PADDLE -> "_";
            case BALL -> "*";
        };
    }
}

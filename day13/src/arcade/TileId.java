package arcade;

public enum TileId {
    EMPTY, WALL, BLOCK, Horizontal_PADDLE, BALL;

    public static TileId fromInt(int n) {
        return switch(n) {
            case 0 -> EMPTY;
            case 1 -> WALL;
            case 2 -> BLOCK;
            case 3 -> Horizontal_PADDLE;
            case 4 -> BALL;
            default -> throw new IllegalStateException("Should not happen");
        };
    }
}

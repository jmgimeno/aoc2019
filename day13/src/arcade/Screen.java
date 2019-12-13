package arcade;

import java.util.HashMap;
import java.util.Map;

public class Screen {

    Map<Position, TileId> tiles = new HashMap<>();

    public TileId get(Position position) {
        return tiles.get(position);
    }

    public void draw(Position position, TileId tileId) {
        tiles.put(position, tileId);
    }

    public long numBlocks() {
        return tiles.values().stream()
                .filter(TileId.BLOCK::equals)
                .count();
    }
/*
    public String render() {
        var whites = tiles.keySet().stream()
                .filter(pos -> tiles.get(pos) == Color.WHITE)
                .collect(Collectors.toUnmodifiableList());
        var screen = screen(whites);
        return render(screen);
    }

    private boolean[][] screen(List<Position> whites) {
        var topMost = whites.stream().mapToInt(pos -> pos.y).min().orElseThrow();
        var leftMost = whites.stream().mapToInt(pos -> pos.x).min().orElseThrow();
        var bottomMost = whites.stream().mapToInt(pos -> pos.y).max().orElseThrow();
        var rightMost = whites.stream().mapToInt(pos -> pos.x).max().orElseThrow();
        var matrix = new boolean[bottomMost-topMost+1][rightMost-leftMost+1];
        whites.forEach(position -> matrix[position.y-topMost][position.x-leftMost] = true);
        return matrix;
    }

    private String render(boolean[][] matrix) {
        var builder = new StringBuilder();
        for (boolean[] booleans : matrix) {
            for (boolean aBoolean : booleans)
                builder.append(aBoolean ? "*" : " ");
            builder.append("\n");
        }
        return builder.toString();
    }

 */
}

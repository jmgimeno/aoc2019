package arcade;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Screen {

    Map<Position, TileId> tiles = new HashMap<>();
    int score;

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

    public void updateScore(int points) {
        score += points;
    }

    public String render() {
        var screen = screen(tiles.keySet());
        return render(screen);
    }

    private TileId[][] screen(Set<Position> positions) {
        var topMost = positions.stream().mapToInt(pos -> pos.y).min().orElseThrow();
        var leftMost = positions.stream().mapToInt(pos -> pos.x).min().orElseThrow();
        var bottomMost = positions.stream().mapToInt(pos -> pos.y).max().orElseThrow();
        var rightMost = positions.stream().mapToInt(pos -> pos.x).max().orElseThrow();
        var matrix = new TileId[bottomMost-topMost+1][rightMost-leftMost+1];
        positions.forEach(position ->
                matrix[position.y-topMost][position.x-leftMost] = tiles.get(position));
        return matrix;
    }

    private String render(TileId[][] matrix) {
        var builder = new StringBuilder();
        builder.append(String.format("Score = %d%n", score));
        for (var tiles : matrix) {
            for (var tile : tiles)
                builder.append(Objects.requireNonNullElse(tile, TileId.EMPTY).render());
            builder.append("\n");
        }
        return builder.toString();
    }
}

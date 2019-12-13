package robot;

import robot.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Grid {

    Map<Position, Color> panels = new HashMap<>();

    public Color get(Position position) {
        return panels.getOrDefault(position, Color.BLACK);
    }

    public void paint(Position position, Color color) {
        panels.put(position, color);
    }

    public int numPainted() {
        return panels.size();
    }

    public String render() {
        var whites = panels.keySet().stream()
                .filter(pos -> panels.get(pos) == Color.WHITE)
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
}

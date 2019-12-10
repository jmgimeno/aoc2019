import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public class Day10 {

    static class Point {
        final int x;
        final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Angle angleTo(Point other) {
            return new Angle(other.x - x, other.y - y);
        }

        int distance(Point other) {
            return Math.abs(x - other.x) + Math.abs(y - other.y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    static class Angle {

        final int dx;
        final int dy;

        public Angle(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        private boolean sameSign(int n1, int n2) {
            return Integer.signum(n1) == Integer.signum(n2);
        }

        @Override
        public boolean equals(Object other) {
            if (other == null) return false;
            if (!(other instanceof Angle)) return false;
            Angle otherAngle = (Angle) other;
            return sameSign(dx, otherAngle.dx)
                    && sameSign(dy, otherAngle.dy)
                    && dx * otherAngle.dy == otherAngle.dx * dy;
        }

        @Override
        public int hashCode() {
            // Seems to work but dangerous
            return Double.valueOf((double) Math.abs(dx) / Math.abs(dy)).hashCode();
        }

        @Override
        public String toString() {
            return "Angle{" +
                    "dx=" + dx +
                    ", dy=" + dy +
                    '}';
        }
    }

    public static class Region {
        final List<Point> asteroids;

        public Region(String map) {
            asteroids = new ArrayList<>();
            int y = 0;
            for (var line : map.split("\n")) {
                for (int x = 0; x < line.length(); x++) {
                    if (line.charAt(x) == '#')
                        asteroids.add(new Point(x, y));
                }
                y += 1;
            }
        }

        int numFrom(Point reference) {
            return asteroids.stream()
                    .filter(not(reference::equals))
                    .map(reference::angleTo)
                    .collect(Collectors.toSet()).size();
        }

        public int maxDetected() {
            return asteroids.stream()
                    .mapToInt(this::numFrom)
                    .max()
                    .orElseThrow();
        }

        public Point maxPoint() {
            return null;
        }
    }

    static void part1() throws IOException {
        var map = Files.readString(Paths.get("input.txt")).trim();
        var region = new Region(map);
        var part1 = region.maxDetected();
        System.out.println("part1 = " + part1);
    }

    public static void main(String[] args) throws IOException {
        part1();
    }
}

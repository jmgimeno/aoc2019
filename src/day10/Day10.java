package day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
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

        static double angleToVertical(int dx, int dy) {
            // y axis is inverted
            if (dx == 0) return dy < 0 ? 0.0 : Math.PI;                       // y-axis
            else if (dy == 0) return dx > 0 ? Math.PI / 2 : 3 * Math.PI / 2;  // x-axis
            double phi = Math.atan(Math.abs((double) dx / dy));               // in (0,PI/2)
            if (dx > 0 && dy < 0) return phi;                                 // 1st quadrant
            else if (dx > 0) return Math.PI - phi;                            // 2nd quadrant
            else if (dy > 0) return Math.PI + phi;                            // 3rd quadrant
            else return 2 * Math.PI - phi;                                    // 4th quadrant
        }

        double angle(Point other) {
            return angleToVertical(other.x - x, other.y - y);
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
                    .map(reference::angle)
                    .collect(Collectors.toSet()).size();
        }

        public int maxDetected() {
            return asteroids.stream()
                    .mapToInt(this::numFrom)
                    .max()
                    .orElseThrow();
        }

        public Point maxPoint() {
            return asteroids.stream()
                    .max(Comparator.comparingInt(this::numFrom))
                    .orElseThrow();
        }

        public List<Point> findTargets(Point laser, int maxSize) {
            var groups = asteroids.stream()
                            .filter(not(laser::equals))
                            .sorted(Comparator.comparing(laser::distance))
                            .collect(Collectors.groupingBy(laser::angle));
            var traverse = new ArrayList<Point>();
            var sortedAngles = groups.keySet().stream()
                    .sorted()
                    .collect(Collectors.toCollection(LinkedList::new));
            do {
                var itAngles = sortedAngles.iterator();
                while (itAngles.hasNext()) {
                    var angle = itAngles.next();
                    var pointsAtAngle = groups.get(angle);
                    var closestPoint = pointsAtAngle.remove(0);
                    if (pointsAtAngle.isEmpty()) {
                        groups.remove(angle);
                        itAngles.remove();
                    }
                    traverse.add(closestPoint);
                    if (traverse.size() == maxSize)
                        return traverse;
                }
            } while (!groups.isEmpty());
            return traverse;
        }
    }

    static Point findLaser(String map) {
        int y = 0;
        for (var line : map.split("\n")) {
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == 'X')
                    return new Point(x, y);
            }
            y += 1;
        }
        throw new IllegalStateException("Should not happen");
    }

    static void part1() throws IOException {
        var map = Files.readString(Paths.get("data/day10-input.txt")).trim();
        var region = new Region(map);
        var point = region.maxPoint();
        var num = region.maxDetected();
        System.out.println("part1 = " + point + " (" + num + ")");
    }

    static void part2() throws IOException {
        var map = Files.readString(Paths.get("data/day10-input.txt")).trim();
        var region = new Region(map);
        var laser = region.maxPoint();
        var targets = region.findTargets(laser, 200);
        var target200 = targets.get(199);
        System.out.println("target200 = " + target200);
        var part2 = 100 * target200.x + target200.y;
        System.out.println("part2 = " + part2);
    }

    public static void main(String[] args) throws IOException {
        part1();
        part2();
    }
}
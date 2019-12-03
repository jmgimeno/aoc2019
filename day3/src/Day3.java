import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day3 {

    static class Point implements Comparable<Point> {

        final int x;
        final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Point other) {
            return Integer.compare(toOrigin(), other.toOrigin());
        }

        int toOrigin() {
            return Math.abs(x) + Math.abs(y);
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    static class Line {

        public int length() {
            return max - min;
        }

        enum Orientation {VERTICAL, HORIZONTAL}

        final Orientation direction;

        final int start, end, along;
        final int min, max;

        private Line(Orientation direction, int start, int end, int along) {
            this.direction = direction;
            this.start = start;
            this.end = end;
            this.along = along;
            if (start <= end) {
                this.min = start;
                this.max = end;
            } else {
                this.min = end;
                this.max = start;
            }
        }

        static Line vertical(int start, int end, int along) {
            return new Line(Orientation.VERTICAL, start, end, along);
        }

        static Line horizontal(int start, int end, int along) {
            return new Line(Orientation.HORIZONTAL, start, end, along);
        }

        Optional<Point> cross(Line other) {
            if (direction == other.direction)
                return Optional.empty();
            else if (direction == Orientation.VERTICAL)
                return other.cross(this);
            else if (min < other.along && other.along < max && other.min < along && along < other.max)
                return Optional.of(new Point(other.along, along));
            else
                return Optional.empty();
        }

        static List<Line> fromPath(String path) {
            var movements = Movement.fromPath(path);
            Point current = new Point(0, 0);
            List<Line> lines = new ArrayList<>();
            for (Movement movement : movements) {
                switch (movement.direction) {
                    case R -> {
                        lines.add(horizontal(current.x, current.x + movement.amount, current.y));
                        current = new Point(current.x + movement.amount, current.y);
                    }
                    case L -> {
                        lines.add(horizontal(current.x, current.x - movement.amount, current.y));
                        current = new Point(current.x - movement.amount, current.y);
                    }
                    case U -> {
                        lines.add(vertical(current.y, current.y + movement.amount, current.x));
                        current = new Point(current.x, current.y + movement.amount);
                    }
                    case D -> {
                        lines.add(vertical(current.y, current.y - movement.amount, current.x));
                        current = new Point(current.x, current.y - movement.amount);
                    }
                }
            }
            return lines;
        }

        static class Movement {

            enum Direction {R, D, L, U}

            final Direction direction;

            final int amount;

            private Movement(Direction direction, int amount) {
                this.direction = direction;
                this.amount = amount;
            }

            static Movement parseMovement(String str) {
                var m = str.substring(0, 1);
                var a = str.substring(1);
                return new Movement(Direction.valueOf(m), Integer.parseInt(a));
            }

            static List<Movement> fromPath(String path) {
                return Stream.of(path.split(","))
                        .map(Movement::parseMovement)
                        .collect(Collectors.toCollection(ArrayList::new));
            }

        }

        int steps(Point point) {
            // point is over the line
            return switch (direction) {
                case VERTICAL -> Math.abs(point.y - start);
                case HORIZONTAL -> Math.abs(point.x - start);
            };
        }

        boolean contains(Point point) {
            return switch (direction) {
                case VERTICAL -> point.x == along && min < point.y && point.y < max;
                case HORIZONTAL -> point.y == along && min < point.x && point.x < max;
            };
        }
    }

    static int part1(String path1, String path2) {
        List<Line> lines1 = Line.fromPath(path1);
        List<Line> lines2 = Line.fromPath(path2);
        return lines1.stream().flatMap(line1 ->
                lines2.stream().map(line1::cross))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .mapToInt(Point::toOrigin)
                .min()
                .orElseThrow(() -> new IllegalStateException("Should not happen"));
    }

    static void part1() {
        try (var file = Files.newBufferedReader(Paths.get("input1.txt"))) {
            var line1 = file.readLine();
            var line2 = file.readLine();
            var part1 = part1(line1, line2);
            System.out.println("part1 = " + part1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static int steps(Point target, List<Line> lines) {
        // target is on one of the lines
        int steps = 0;
        for (Line line : lines) {
            if (line.contains(target)) {
                return steps + line.steps(target);
            } else {
                steps += line.length();
            }
        }
        throw new IllegalStateException("Should not happen");
    }

    static int part2(String path1, String path2) {
        var lines1 = Line.fromPath(path1);
        var lines2 = Line.fromPath(path2);
        return lines1.stream().flatMap(line1 ->
                lines2.stream().map(line1::cross))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .mapToInt(p -> steps(p, lines1) + steps(p, lines2))
                .min()
                .orElseThrow(() -> new IllegalStateException("Should not happen"));
    }

    static void part2() {
        try (var file = Files.newBufferedReader(Paths.get("input1.txt"))) {
            var line1 = file.readLine();
            var line2 = file.readLine();
            var part2 = part2(line1, line2);
            System.out.println("part2 = " + part2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        part1();
        part2();
    }
}

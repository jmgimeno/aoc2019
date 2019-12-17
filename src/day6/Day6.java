package day6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day6 {

    static long totalOrbits(String fileName) throws IOException {
        return loadOrbits(fileName).totalOrbits();
    }

    static Orbits loadOrbits(String fileName) throws IOException {
        var orbits = new Orbits();
        Files.lines(Paths.get(fileName))
                .forEach(line -> {
                    var st = new StringTokenizer(line, ")");
                    var center = st.nextToken();
                    var orbiter = st.nextToken();
                    orbits.add(center, orbiter);
                });
        return orbits;
    }

    static void part1() throws IOException {
        System.out.println("part1 = " + totalOrbits("data/day6-day7-day8-day9-day10-day11-day12-day13-day14-day15-day16-day17-input.txt"));
    }

    public static long totalTransfers(String fileName) throws IOException {
        var orbits = loadOrbits(fileName);
        var pathSanta = orbits.pathToRoot("SAN");
        var pathYou = orbits.pathToRoot("YOU");
        return pathSanta.size() < pathYou.size()
                ? totalTransfers(pathSanta, pathYou)
                : totalTransfers(pathYou, pathSanta);
    }

    static <E> Set<E> listToSet(List<E> list) {
        return list.stream().collect(Collectors.toUnmodifiableSet());
    }

    private static long totalTransfers(List<String> shortPathToRoot, List<String> longPathToRoot) {
        var objectsInLongPath = listToSet(longPathToRoot);
        var common = shortPathToRoot.stream()
                .filter(objectsInLongPath::contains)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("ShouldNotHappen"));
        return shortPathToRoot.indexOf(common) + longPathToRoot.indexOf(common) - 2L;
    }

    static void part2() throws IOException {
        System.out.println("part2 = " + totalTransfers("data/day6-day7-day8-day9-day10-day11-day12-day13-day14-day15-day16-day17-input.txt"));
    }

    public static void main(String[] args) throws IOException {
        part1();
        part2();
    }
}

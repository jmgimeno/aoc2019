package day12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Day12 {

    private static void part1() throws IOException {
        var moons = Files.lines(Paths.get("data/day12-input.txt"))
                .map(Vector::parseVector)
                .map(Moon::new)
                .collect(Collectors.toUnmodifiableList());
        Cluster cluster = new Cluster(moons);
        cluster = cluster.step(1000);
        System.out.println("part1 = " + cluster.totalEnergy());
    }

    private static void part2() throws IOException {
        var moons = Files.lines(Paths.get("data/day12-test.txt"))
                .map(Vector::parseVector)
                .map(Moon::new)
                .collect(Collectors.toUnmodifiableList());
        var steps = stepsToRepetition(moons);
        System.out.println("part2 = " + steps);
    }

    static long stepsToRepetition(List<Moon> moons) {
        var cluster = new Cluster(moons);
        var history = new HashSet<Cluster>();
        var steps = 0L;
        history.add(cluster);
        while (true) {
            steps += 1L;
            cluster = cluster.step();
            if (!history.add(cluster))
                return steps;
        }
    }

    public static void main(String[] args) throws IOException {
        part1();
        part2();
    }
}

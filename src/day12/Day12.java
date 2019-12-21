package day12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day12 {

    private static void part1() throws IOException {
        var moons = readMoons("data/day12-input.txt");
        Cluster<Vector3> cluster = new Cluster<>(moons);
        cluster = cluster.step(1000);
        System.out.println("part1 = " + cluster.totalEnergy());
    }

    private static void part2() throws IOException {
        var moons = readMoons("data/day12-input.txt");
        var part2 = stepsToRepetition(moons);
        System.out.println("part2 = " + part2);
    }

    static List<Moon<Vector3>> readMoons(String fileName) throws IOException {
        return Files.lines(Paths.get(fileName))
                .map(Vector3::parseVector)
                .map(Moon::new)
                .collect(Collectors.toUnmodifiableList());
    }

    private static List<Moon<Vector1>> project(List<Moon<Vector3>> moons2, Function<Vector3, Vector1> projector) {
        return moons2.stream()
                .map(Moon::getPosition)
                .map(projector)
                .map(Moon::new)
                .collect(Collectors.toUnmodifiableList());
    }

    static long stepsToRepetition(List<Moon<Vector3>> moons) {
        var xs = project(moons, Vector3::getX);
        var clusterX = new Cluster<>(xs);
        var stepsX = clusterX.stepsToZeroVelocity();
        var ys = project(moons, Vector3::getY);
        var clusterY = new Cluster<>(ys);
        var stepsY = clusterY.stepsToZeroVelocity();
        var zs = project(moons, Vector3::getZ);
        var clusterZ = new Cluster<>(zs);
        var stepsZ = clusterZ.stepsToZeroVelocity();
        return 2 * lcm(stepsX, stepsY, stepsZ);
    }

    static long lcm(long a, long b, long c) {
        return lcm(a, lcm(b, c));
    }

    static long lcm(long a, long b) {
        return (a * b) / gcd(a, b);
    }

    static long gcd(long a, long b) {
        long r = a % b;
        while (r != 0L) {
            a = b;
            b = r;
            r = a % b;
        }
        return b;
    }

    public static void main(String[] args) throws IOException {
        part1();
        part2();
    }
}

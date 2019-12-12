import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Day12 {

    private static void part1() throws IOException {
        var moons = Files.lines(Paths.get("input.txt"))
                .map(Vector::parseVector)
                .map(Moon::new)
                .collect(Collectors.toUnmodifiableList());
        Cluster cluster = new Cluster(moons);
        cluster.step(1000);
        System.out.println("part1 = " + cluster.totalEnergy());
    }

    public static void main(String[] args) throws IOException {
        part1();
    }
}

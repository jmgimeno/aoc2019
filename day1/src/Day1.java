import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day1 {

    public static int requiredFuel(int mass) {
        return mass / 3 - 2;
    }

    public static void part1() throws IOException {
        int part1 = Files.lines(Paths.get("input1.txt"))
                .mapToInt(Integer::parseInt)
                .map(Day1::requiredFuel)
                .sum();
        System.out.println("part1 = " + part1);
    }

    public static IntStream totalFuelStream(int initialMass) {
        return Stream.iterate(initialMass, Day1::requiredFuel)
                .mapToInt(Integer::intValue)
                .skip(1)
                .takeWhile(fuel -> fuel > 0);
    }

    public static int totalFuel(int initialMass) {
        return totalFuelStream(initialMass).sum();
    }

    public static void part2() throws IOException {
        int part2 = Files.lines(Paths.get("input2.txt"))
                .mapToInt(Integer::parseInt)
                .flatMap(Day1::totalFuelStream)
                .sum();
        System.out.println("part2 = " + part2);
    }

    public static void main(String[] args) throws IOException {
        part1();
        part2();
    }
}

package day14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day14 {
    public static int minimumRequiredOre(String lines) {
        Reactions reactions = Reactions.fromString(lines);
        return reactions.inputQuantity(1, "FUEL", "ORE");
    }

    private static void part1() throws IOException {
        var lines = Files.readString(Paths.get("data/day14-input.txt")).trim();
        var part1 = minimumRequiredOre(lines);
        System.out.println("part1 = " + part1);
    }
    public static void main(String[] args) throws IOException {
        part1();
    }
}

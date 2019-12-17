package day16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day16 {

    public static void part1() throws IOException {
        var input = Files.readString(Paths.get("data/day16-day17-input.txt")).trim();
        var part1 = FFT.firstEight(input, 100);
        System.out.println("part1 = " + part1);
    }
    public static void main(String[] args) throws IOException {
        part1();
    }
}

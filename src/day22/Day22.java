package day22;

import java.io.IOException;
import java.nio.file.Paths;

public class Day22 {

    private static void part1() throws IOException {
        var deck = Parser.parse(Paths.get("data/day22-input.txt"), 10007L);
        var part1 = deck.direct(2019);
        System.out.println("part1 = " + part1);
    }

    private static void part2() throws IOException {
        var deck = Parser.parse(Paths.get("data/day22-input.txt"), 119315717514047L);
        var repeatedShuffle = Deck.repeat(deck, 101741582076661L);
        var part2 = repeatedShuffle.inverse(2020L);
        System.out.println("part2 = " + part2);
    }

    public static void main(String[] args) throws IOException {
        part1();
        part2();
    }
}

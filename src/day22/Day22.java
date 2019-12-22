package day22;

import java.io.IOException;
import java.nio.file.Paths;

public class Day22 {

    private static void part1() throws IOException {
        var shuffle = Parser.parse(Paths.get("data/day22-input.txt"));
        var deck = new Deck(10007L);
        var shuffledDeck = shuffle.apply(deck);
        var part1 = shuffledDeck.positionOf(2019);
        System.out.println("part1 = " + part1);
    }

    private static void part2() throws IOException {
        var shuffle = Parser.parse(Paths.get("data/day22-input.txt"));
        var deck = new Deck(119315717514047L);
        var shuffledDeck = shuffle.apply(deck);
        var part2 = shuffledDeck.cardAt(2020);
        System.out.println("part2 = " + part2);
    }
    public static void main(String[] args) throws IOException {
        part1();
        part2();
    }
}

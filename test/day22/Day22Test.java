package day22;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Day22Test {

    @Test
    void intoNewStack() {
        var deck = new Deck(10);
        var expected = new Deck(List.of(9, 8, 7, 6, 5, 4, 3, 2, 1, 0));
        assertEquals(expected, deck.intoNewStack());
    }

    @Test
    void cutNPositive() {
        var deck = new Deck(10);
        var expected = new Deck(List.of(3, 4, 5, 6, 7, 8, 9, 0, 1, 2));
        assertEquals(expected, deck.cutNCards(3));
    }

    @Test
    void cutNNegative() {
        var deck = new Deck(10);
        var expected = new Deck(List.of(6, 7, 8, 9, 0, 1, 2, 3, 4, 5));
        assertEquals(expected, deck.cutNCards(-4));
    }

    @Test
    void withIncrement() {
        var deck = new Deck(10);
        var expected = new Deck(List.of(0, 7, 4, 1, 8, 5, 2, 9, 6, 3));
        assertEquals(expected, deck.withIncrement(3));
    }

    @Test
    void part1Example1() throws IOException {
        var shuffle = Parser.parse(Paths.get("data/day22-test1.txt"));
        var deck = new Deck(10);
        var expected = new Deck(List.of(0, 3, 6, 9, 2, 5, 8, 1, 4, 7));
        assertEquals(expected, shuffle.apply(deck));
    }

    @Test
    void part1Example2() throws IOException {
        var shuffle = Parser.parse(Paths.get("data/day22-test2.txt"));
        var deck = new Deck(10);
        var expected = new Deck(List.of(3, 0, 7, 4, 1, 8, 5, 2, 9, 6));
        assertEquals(expected, shuffle.apply(deck));
    }

    @Test
    void part1Example3() throws IOException {
        var shuffle = Parser.parse(Paths.get("data/day22-test3.txt"));
        var deck = new Deck(10);
        var expected = new Deck(List.of(6, 3, 0, 7, 4, 1, 8, 5, 2, 9));
        assertEquals(expected, shuffle.apply(deck));
    }

    @Test
    void part1Example4() throws IOException {
        var shuffle = Parser.parse(Paths.get("data/day22-test4.txt"));
        var deck = new Deck(10);
        var expected = new Deck(List.of(9, 2, 5, 8, 1, 4, 7, 0, 3, 6));
        assertEquals(expected, shuffle.apply(deck));
    }
}
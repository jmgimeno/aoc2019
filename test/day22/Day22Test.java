package day22;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day22Test {

    @Test
    void intoNewStack() {
        Deck deck = Deck.intoNewStack(10L);
        var expected = List.of(9L, 8L, 7L, 6L, 5L, 4L, 3L, 2L, 1L, 0L);
        assertEquals(expected, deck.toList());
    }

    @Test
    void cutNPositive() {
        Deck deck = Deck.cutNCards(3L, 10L);
        var expected = List.of(3L, 4L, 5L, 6L, 7L, 8L, 9L, 0L, 1L, 2L);
        assertEquals(expected, deck.toList());
    }

    @Test
    void cutNNegative() {
        Deck deck = Deck.cutNCards(-4L, 10L);
        var expected = List.of(6L, 7L, 8L, 9L, 0L, 1L, 2L, 3L, 4L, 5L);
        assertEquals(expected, deck.toList());
    }

    @Test
    void withIncrement() {
        Deck deck = Deck.withIncrement(3L, 10L);
        var expected = List.of(0L, 7L, 4L, 1L, 8L, 5L, 2L, 9L, 6L, 3L);
        assertEquals(expected, deck.toList());
    }

    @Test
    void part1Example1() throws IOException {
        var deck = Parser.parse(Paths.get("data/day22-test1.txt"), 10L);
        var expected = List.of(0L, 3L, 6L, 9L, 2L, 5L, 8L, 1L, 4L, 7L);
        assertEquals(expected, deck.toList());
    }

    @Test
    void part1Example2() throws IOException {
        var deck = Parser.parse(Paths.get("data/day22-test2.txt"), 10L);
        var expected = List.of(3L, 0L, 7L, 4L, 1L, 8L, 5L, 2L, 9L, 6L);
        assertEquals(expected, deck.toList());
    }

    @Test
    void part1Example3() throws IOException {
        var deck = Parser.parse(Paths.get("data/day22-test3.txt"), 10L);
        var expected = List.of(6L, 3L, 0L, 7L, 4L, 1L, 8L, 5L, 2L, 9L);
        assertEquals(expected, deck.toList());
    }

    @Test
    void part1Example4() throws IOException {
        var deck = Parser.parse(Paths.get("data/day22-test4.txt"), 10L);
        var expected = List.of(9L, 2L, 5L, 8L, 1L, 4L, 7L, 0L, 3L, 6L);
        assertEquals(expected, deck.toList());
    }

    @Test
    void part1() throws IOException {
        var deck = Parser.parse(Paths.get("data/day22-input.txt"), 10007L);
        assertEquals(7860L, deck.direct(2019L));
    }
}
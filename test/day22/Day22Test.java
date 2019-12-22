package day22;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day22Test {

    @Test
    void intoNewStack() {
        var deck = new Deck(10L);
        var expected = List.of(9L, 8L, 7L, 6L, 5L, 4L, 3L, 2L, 1L, 0L);
        Deck shuffleDeck = deck.intoNewStack();
        assertEquals(expected, shuffleDeck.toList());
    }

    @Test
    void cutNPositive() {
        var deck = new Deck(10L);
        var expected = List.of(3L, 4L, 5L, 6L, 7L, 8L, 9L, 0L, 1L, 2L);
        Deck shuffleDeck = deck.cutNCards(3);
        assertEquals(expected, shuffleDeck.toList());
    }

    @Test
    void cutNNegative() {
        var deck = new Deck(10L);
        var expected = List.of(6L, 7L, 8L, 9L, 0L, 1L, 2L, 3L, 4L, 5L);
        Deck shuffleDeck = deck.cutNCards(-4);
        assertEquals(expected, shuffleDeck.toList());
    }

    @Test
    void withIncrement() {
        var deck = new Deck(10L);
        var expected = List.of(0L, 7L, 4L, 1L, 8L, 5L, 2L, 9L, 6L, 3L);
        Deck shuffleDeck = deck.withIncrement(3);
        assertEquals(expected, shuffleDeck.toList());
    }

    @Test
    void part1Example1() throws IOException {
        var shuffle = Parser.parse(Paths.get("data/day22-test1.txt"));
        var deck = new Deck(10L);
        var expected = List.of(0L, 3L, 6L, 9L, 2L, 5L, 8L, 1L, 4L, 7L);
        Deck shuffleDeck = shuffle.apply(deck);
        assertEquals(expected, shuffleDeck.toList());
    }

    @Test
    void part1Example2() throws IOException {
        var shuffle = Parser.parse(Paths.get("data/day22-test2.txt"));
        var deck = new Deck(10L);
        var expected = List.of(3L, 0L, 7L, 4L, 1L, 8L, 5L, 2L, 9L, 6L);
        Deck shuffleDeck = shuffle.apply(deck);
        assertEquals(expected, shuffleDeck.toList());
    }

    @Test
    void part1Example3() throws IOException {
        var shuffle = Parser.parse(Paths.get("data/day22-test3.txt"));
        var deck = new Deck(10L);
        var expected = List.of(6L, 3L, 0L, 7L, 4L, 1L, 8L, 5L, 2L, 9L);
        Deck shuffleDeck = shuffle.apply(deck);
        assertEquals(expected, shuffleDeck.toList());
    }

    @Test
    void part1Example4() throws IOException {
        var shuffle = Parser.parse(Paths.get("data/day22-test4.txt"));
        var deck = new Deck(10L);
        var expected = List.of(9L, 2L, 5L, 8L, 1L, 4L, 7L, 0L, 3L, 6L);
        Deck shuffleDeck = shuffle.apply(deck);
        assertEquals(expected, shuffleDeck.toList());
    }

    @Test
    void part1() throws IOException {
        var shuffle = Parser.parse(Paths.get("data/day22-input.txt"));
        var deck = new Deck(10007L);
        var shuffledDeck = shuffle.apply(deck);
        assertEquals(7860L, shuffledDeck.positionOf(2019L));
    }

    @Test
    void repeat1() {
        Function<Deck, Deck> shuffle = Deck::intoNewStack;
        var deck = new Deck(119315717514047L);
        var repeatedShuffle = Deck.repeat(shuffle, 10000L);
        var shuffledDeck = repeatedShuffle.apply(deck);
        assertEquals(2020L, shuffledDeck.cardAt(2020L));
    }

    @Test
    void repeat2() {
        Function<Deck, Deck> shuffle = Deck::intoNewStack;
        var deck = new Deck(119315717514047L);
        var repeatedShuffle = Deck.repeat(shuffle, 10001L);
        var shuffledDeck = repeatedShuffle.apply(deck);
        assertEquals(119315717514047L - 1L - 2020L, shuffledDeck.cardAt(2020L));
    }
}
package day22;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Deck {
    private final int[] cards;

    public Deck(int size) {
        this.cards = IntStream.range(0, size).toArray();
    }

    public Deck(List<Integer> cards) {
        this.cards = cards.stream().mapToInt(Integer::intValue).toArray();
    }

    public Deck(int[] cards) {
        this.cards = Arrays.copyOf(cards, cards.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deck deck = (Deck) o;
        return Arrays.equals(cards, deck.cards);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(cards);
    }

    @Override
    public String toString() {
        return "Deck{" +
                "cards=" + Arrays.toString(cards) +
                '}';
    }

    public Deck intoNewStack() {
        System.out.println("new stack");
        var inverted = IntStream.range(0, cards.length)
                .map(i -> cards[cards.length - 1 - i])
                .toArray();
        return new Deck(inverted);
    }

    public Deck cutNCards(int n) {
        System.out.println("cut " + n);
        var  nn = (n >= 0) ? n : n + cards.length;
        var cut = IntStream.range(0, cards.length)
                .map(i -> cards[(i + nn) % cards.length])
                .toArray();
        return new Deck(cut);
    }

    public Deck withIncrement(int n) {
        System.out.println("increment " + n);
        var newCards = new int[cards.length];
        var j = 0;
        for (int i = 0; i < cards.length; i++) {
            newCards[j] = cards[i];
            j = (j + n) % newCards.length;
        }
        return new Deck(newCards);
    }

    public int cardAt(int i) {
        return cards[i];
    }
}

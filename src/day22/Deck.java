package day22;

import java.math.BigInteger;
import java.util.List;
import java.util.function.Function;
import java.util.function.LongUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Deck {
    private final long size;
    private final LongUnaryOperator cards;

    public Deck(long size) {
        this.size = size;
        this.cards = LongUnaryOperator.identity();
    }

    public Deck(long size, LongUnaryOperator cards) {
        this.size = size;
        this.cards = cards;
    }

    public Deck intoNewStack() {
        LongUnaryOperator inverted = i -> cards.applyAsLong(size - 1 - i);
        return new Deck(size, inverted);
    }

    public Deck cutNCards(long n) {
        var nn = (n >= 0L) ? n : n + size;
        LongUnaryOperator cut = i -> cards.applyAsLong((i + nn) % size);
        return new Deck(size, cut);
    }

    public Deck withIncrement(long n) {
        var bigSize = BigInteger.valueOf(size);
        var bigN = BigInteger.valueOf(n);
        var invN = bigN.modInverse(bigSize);
        LongUnaryOperator withIncrement = i -> {
            var bigI = BigInteger.valueOf(i);
            var bigInvI = invN.multiply(bigI).mod(bigSize);
            return cards.applyAsLong(bigInvI.longValue());
        };
        return new Deck(size, withIncrement);
    }

    public static Function<Deck, Deck> repeat(Function<Deck, Deck> base, long exponent) {
        System.out.println("exponent = " + exponent);
        if (exponent == 0L) {
            return Function.identity();
        } else if (exponent == 1L) {
            return base;
        } else {
            var half = repeat(base.andThen(base), exponent / 2L);
            return exponent % 2L == 0L ? half : half.andThen(base);
        }
    }

    public long positionOf(long n) {
        return LongStream.range(0, size)
                .filter(i -> cards.applyAsLong(i) == n)
                .findFirst()
                .orElseThrow();
    }

    public long cardAt(long i) {
        return cards.applyAsLong(i);
    }

    public List<Long> toList() {
        return LongStream.range(0, size)
                .mapToObj(cards::applyAsLong)
                .collect(Collectors.toUnmodifiableList());
    }
}

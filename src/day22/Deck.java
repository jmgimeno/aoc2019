package day22;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Deck {
    // A deck is a map i -> a i + b (mod n)

    private final BigInteger a;
    private final BigInteger b;
    private final BigInteger n;

    private Deck(BigInteger a, BigInteger b, BigInteger n) {
        this.a = a;
        this.b = b;
        this.n = n;
    }

    public Deck(long n) {
        this(BigInteger.ONE, BigInteger.ZERO, BigInteger.valueOf(n));
    }

    public static Deck intoNewStack(long n) {
        // i -> -i + n - 1 (mod n)
        var bigN = BigInteger.valueOf(n);
        return new Deck(
                BigInteger.ONE.negate(),
                bigN.subtract(BigInteger.ONE),
                bigN);
    }

    public static Deck cutNCards(long k, long n) {
        // i -> i - k (mod n)
        return new Deck(
                BigInteger.ONE,
                BigInteger.valueOf(-k),
                BigInteger.valueOf(n));
    }

    public static Deck withIncrement(long k, long n) {
        // i -> k * i (mod n)
        return new Deck(
                BigInteger.valueOf(k),
                BigInteger.ZERO,
                BigInteger.valueOf(n));
    }

    public Deck andThen(Deck other) {
        // this:  f x = f.a x + f.b (mod n)
        // other: g x = g.a x + g.b (mod n)
        // f andThen g = g (f x) = g.a (f x) + g.b = g.a (f.a x + f.b) + g.b
        //             = (g.a * f.a) x + (g.a f.b + g.b)
        var newA = other.a.multiply(a).mod(n);
        var newB = other.a.multiply(b).add(other.b).mod(n);
        return new Deck(newA, newB, n);
    }

    public static Deck repeat(Deck base, long exponent) {
        if (exponent == 0L) {
            return new Deck(BigInteger.ONE, BigInteger.ZERO, base.n);
        } else {
            var half = repeat(base.andThen(base), exponent / 2L);
            return exponent % 2L == 0L ? half : half.andThen(base);
        }
    }

    public long direct(long i) {
        // a * i + b (mod n)
        var bigI = BigInteger.valueOf(i);
        return a.multiply(bigI).add(b).mod(n).longValue();
    }

    public long inverse(long i) {
        // i = a j + b => j = (1/a)(i - b)
        var bigI = BigInteger.valueOf(i);
        var invA = a.modInverse(n);
        return bigI.subtract(b).multiply(invA).mod(n).longValue();
    }

    public List<Long> toList() {
        return LongStream.range(0, n.longValue())
                .mapToObj(this::inverse)
                .collect(Collectors.toUnmodifiableList());
    }
}

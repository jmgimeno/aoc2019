package day22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Parser {

    private static final String NEW = "deal into new stack";
    private static final String CUT = "cut ";
    private static final String DEAL = "deal with increment ";

    public static Deck parse(String line, long n) {
        if (line.startsWith(NEW)) {
            return Deck.intoNewStack(n);
        } else if (line.startsWith(CUT)) {
            var k = Long.parseLong(line.substring(CUT.length()));
            return Deck.cutNCards(k, n);
        } else if (line.startsWith(DEAL)) {
            var k = Long.parseLong(line.substring(DEAL.length()));
            return Deck.withIncrement(k, n);
        } else {
            throw new IllegalStateException("should not happen");
        }
    }

    public static Deck parse(Path path, long n) throws IOException {
        return Files.lines(path)
                .map(l -> parse(l, n))
                .reduce(new Deck(n), Deck::andThen);
    }
}

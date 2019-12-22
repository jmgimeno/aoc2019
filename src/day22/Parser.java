package day22;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

public class Parser {

    private static final String NEW = "deal into new stack";
    private static final String CUT = "cut ";
    private static final String DEAL = "deal with increment ";

    public static Function<Deck, Deck> parse(String line) {
        if (line.startsWith(NEW)) {
            return Deck::intoNewStack;
        } else if (line.startsWith(CUT)) {
            var n = Integer.parseInt(line.substring(CUT.length()));
            return d -> d.cutNCards(n);
        } else if (line.startsWith(DEAL)) {
            var n = Integer.parseInt(line.substring(DEAL.length()));
            return d -> d.withIncrement(n);
        } else {
            throw new IllegalStateException("should not happen");
        }
    }

    public static Function<Deck, Deck> parse(Path path) throws IOException {
        return Files.lines(path)
                .map(Parser::parse)
                .reduce(Function.identity(), Function::andThen);
    }
}

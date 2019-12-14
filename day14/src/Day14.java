import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day14 {
    public static int minimumRequiredOre(String reactions) {
        List<Rules> rules = Arrays.stream(reactions.split("\n"))
                .map(Rules::parseReaction)
                .collect(Collectors.toUnmodifiableList());
        State state = new State(List.of(new Item(1, "FUEL")));
        while (!state.isFinal()) {
            // state =
        }
        return 0;
    }
}

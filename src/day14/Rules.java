package day14;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public class Rules {

    final Item right;
    final List<Item> left;

    public Rules(Item right, List<Item> left) {
        this.right = right;
        this.left = left;
    }

    public static Rules parseReaction(String s) {
        // 2 VPVL, 7 FWMGM, 2 CXFTF, 11 MNCFX => 1 STKFG
        Pattern pattern = Pattern.compile("(?:, | => )");
        var items = pattern.splitAsStream(s)
                .filter(not(String::isBlank))
                .map(Item::parseItem)
                .collect(Collectors.toCollection(ArrayList::new));
        var right = items.remove(items.size() - 1);
        return new Rules(right, items);
    }

    public boolean isTerminal() {
        return left.stream().allMatch(item -> "ORE".equals(item.name));
    }

    public List<Item> applyFor(Item objective) {
        if (!right.name.equals(objective.name))
            return Collections.emptyList();
        var factor = objective.quantity / right.quantity + (objective.quantity / right.quantity == 0 ? 0 : 1);
        return left.stream().map(item -> item.multiply(factor)).collect(Collectors.toUnmodifiableList());
    }
}

package day14;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public class Reaction {

    private final String outputName;
    private final int outputQuantity;
    private final Map<String, Integer> inputs;

    private Reaction(Item output, List<Item> inputs) {
        this.outputName = output.name;
        this.outputQuantity = output.quantity;
        this.inputs = inputs.stream()
                .collect(Collectors.toMap(Item::getName, Item::getQuantity));
    }

    public String getOutputName() {
        return outputName;
    }

    public int getOutputQuantity() {
        return outputQuantity;
    }

    public Map<String, Integer> getInputs() {
        return inputs;
    }

    private static class Item {
        final String name;
        final int quantity;

        public Item(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public static Item parseItem(String str) {
            // 10 ORE
            var parts = str.split(" ");
            return new Item(parts[1], Integer.parseInt(parts[0]));
        }
    }

    public static Reaction parseReaction(String s) {
        // 2 VPVL, 7 FWMGM, 2 CXFTF, 11 MNCFX => 1 STKFG
        Pattern pattern = Pattern.compile("(?:, | => )");
        var items = pattern.splitAsStream(s)
                .filter(not(String::isBlank))
                .map(Item::parseItem)
                .collect(Collectors.toCollection(ArrayList::new));
        var right = items.remove(items.size() - 1);
        return new Reaction(right, items);
    }

    @Override
    public String toString() {
        return "Reaction{" +
                "outputName='" + outputName + '\'' +
                ", outputQuantity=" + outputQuantity +
                ", inputs=" + inputs +
                '}';
    }
}

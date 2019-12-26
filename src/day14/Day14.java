package day14;

public class Day14 {
    public static int minimumRequiredOre(String lines) {
        Reactions reactions = Reactions.fromString(lines);
        return reactions.inputQuantity(1, "FUEL", "ORE");
    }
}

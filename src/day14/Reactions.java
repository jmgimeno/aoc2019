package day14;

import java.util.*;
import java.util.stream.Collectors;

public class Reactions {

    private final List<Reaction> reactions;

    public Reactions(List<Reaction> reactions) {
        this.reactions = reactions;
    }

    public static Reactions fromString(String lines) {
        var reactions = Arrays.stream(lines.split("\n"))
                .map(Reaction::parseReaction)
                .collect(Collectors.toUnmodifiableList());
        return new Reactions(reactions);
    }

    private static class State {
        private final Map<String, Integer> products;

        public State(int quantity, String name) {
            this.products = new HashMap<>();
            this.products.put(name, quantity);
        }

        public State(Map<String, Integer> products) {
            this.products = products;
        }

        public boolean isFinal(String inputName) {
            return this.products.keySet().stream()
                    .allMatch(inputName::equals);
        }

        public List<State> applyReactions(List<Reaction> reactions) {
            return reactions.stream()
                    .map(this::applyReaction)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toUnmodifiableList());
        }

        private Optional<State> applyReaction(Reaction reaction) {
            String outputName = reaction.getOutputName();
            if (!products.containsKey(outputName)) {
                return Optional.empty();
            }
            int needed = products.get(outputName);
            int produced = reaction.getOutputQuantity();
            int factor = needed / produced + (needed % produced == 0 ? 0 : 1);
            return Optional.of(merge(factor, outputName, reaction.getInputs(), products));
        }

        private State merge(int factor, String output, Map<String, Integer> inputs, Map<String, Integer> products) {
            var newState = new HashMap<>(products);
            newState.remove(output);
            for (String input : inputs.keySet()) {
                var total = factor * inputs.get(input);
                newState.put(input, newState.getOrDefault(input, 0) + total);
            }
            return new State(newState);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return products.equals(state.products);
        }

        @Override
        public int hashCode() {
            return Objects.hash(products);
        }

        @Override
        public String toString() {
            return "State{" +
                    "products=" + products +
                    '}';
        }
    }

    public int inputQuantity(int outputQuantity, String outputName, String inputName) {
        var visited = new HashSet<State>();
        var candidates = new ArrayList<State>();
        candidates.add(new State(outputQuantity, outputName));
        while (!candidates.isEmpty()) {
            var current = candidates.remove(0);
            if (current.isFinal(inputName))
                return current.products.get(inputName);
            if (visited.contains(current)) continue;
            var nextStates = current.applyReactions(reactions);
            candidates.addAll(nextStates);
            visited.add(current);
        }
        throw new IllegalStateException("Should not happen");
    }

    @Override
    public String toString() {
        return "Reactions{" +
                "reactions=" + reactions +
                '}';
    }
}

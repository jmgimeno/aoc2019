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

        public int get(String name) {
            return products.getOrDefault(name, 0);
        }
    }

    private Map<String, List<String>> createPrecedences() {
        var precedences = new HashMap<String, List<String>>();
        reactions.forEach(reaction -> {
            precedences.putIfAbsent(reaction.getOutputName(), new ArrayList<>());
            reaction.getInputs().keySet().forEach(name -> {
                        precedences.putIfAbsent(name, new ArrayList<>());
                        precedences.get(name).add(reaction.getOutputName());
                    });
        });
        return precedences;
    }

    private List<String> topologicalSort() {
        var result = new ArrayList<String>();
        var precedences = createPrecedences();
        while (!precedences.isEmpty()) {
            var key = precedences.keySet().stream()
                    .filter(k -> precedences.get(k).isEmpty())
                    .findAny().orElseThrow();
            result.add(key);
            precedences.remove(key);
            precedences.values().forEach(v -> v.remove(key));
        }
        return result;
    }

    public int inputQuantity(int outputQuantity, String outputName, String inputName) {
        var order = topologicalSort();
        var state = new State(outputQuantity, outputName);
        for (String name : order) {
            var reaction = reactions.stream()
                    .filter(r -> r.getOutputName().equals(name))
                    .findAny();
            if (reaction.isEmpty()) continue;
            var newState = state.applyReaction(reaction.get());
            if (newState.isPresent()) {
                state = newState.get();
            }
        }
        System.out.println("state = " + state);
        return state.get(inputName);
    }
}

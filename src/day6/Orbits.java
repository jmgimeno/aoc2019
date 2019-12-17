package day6;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Orbits {

    final Map<String, String> orbits = new HashMap<>();

    public void add(String center, String orbiter) {
        orbits.put(orbiter, center);
    }

    public long numOrbits(String initial) {
        return Stream
                .iterate(initial, orbits::containsKey, orbits::get)
                .count();
    }

    public long totalOrbits() {
        return orbits.keySet().stream()
                .mapToLong(this::numOrbits)
                .sum();
    }

    public List<String> pathToRoot(String initial) {
        return Stream
                .iterate(initial, orbits::containsKey, orbits::get)
                .collect(Collectors.toUnmodifiableList());
    }
}

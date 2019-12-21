package day12;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day12Test {

    @Test
    void part2Ex1() throws IOException {
        var moons = Files.lines(Paths.get("data/day12-test.txt"))
                .map(Vector3::parseVector)
                .map(Moon::new)
                .collect(Collectors.toUnmodifiableList());
        var steps = Day12.stepsToRepetition(moons);
        assertEquals(2772L, steps);
    }

}
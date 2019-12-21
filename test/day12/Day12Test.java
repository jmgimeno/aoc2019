package day12;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day12Test {


    @Test
    void part2Ex1() throws IOException {
        var moons = Day12.readMoons("data/day12-test1.txt");
        var steps = Day12.stepsToRepetition(moons);
        assertEquals(2772L, steps);
    }

    @Test
    void part2Ex2() throws IOException {
        var moons = Day12.readMoons("data/day12-test2.txt");
        var steps = Day12.stepsToRepetition(moons);
        assertEquals(4686774924L, steps);
    }


}
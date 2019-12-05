import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day5Test {

    @Test
    void part1() throws IOException {
        var program = Files.readString(Paths.get("input.txt")).trim();
        var machine = new Day5.Machine(program);
        assertEquals(List.of(0, 0, 0, 0, 0, 0, 0, 0, 0, 12234644), machine.run(1));
    }

    @Test
    void part2() throws IOException {
        var program = Files.readString(Paths.get("input.txt")).trim();
        var machine = new Day5.Machine(program);
        assertEquals(List.of(3508186), machine.run(5));
    }
}
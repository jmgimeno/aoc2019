package day13;

import day13.newmachine.Machine;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class Day13Test {

    static String executeAsDay2(String program) {
        var machine = new Machine(program);
        machine.run();
        assertTrue(machine.isHalted());
        return machine.dumpLowMemory();
    }

    @Test
    void day2tests() {
        assertEquals("2,0,0,0,99", executeAsDay2("1,0,0,0,99"));
        assertEquals("2,3,0,6,99", executeAsDay2("2,3,0,3,99"));
        assertEquals("2,4,4,5,99,9801", executeAsDay2("2,4,4,5,99,0"));
        assertEquals("30,1,1,4,2,5,6,0,99", executeAsDay2("1,1,1,4,99,5,6,0,99"));
    }

    static List<Long> executeAsDay5(String program, long value) {
        var machine = new Machine(program);
        var result = machine.run();
        assertTrue(machine.isWaiting());
        assertFalse(machine.isHalted());
        machine.addInput(value);
        result.addAll(machine.run());
        assertFalse(machine.isWaiting());
        assertTrue(machine.isHalted());
        return result.stream()
                .collect(Collectors.toUnmodifiableList());
    }

    @Test
    void day5part1() throws IOException {
        var program = Files.readString(Paths.get("data/day5-input.txt")).trim();
        assertEquals(List.of(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 12234644L), executeAsDay5(program, 1L));
    }

    @Test
    void day5Part2() throws IOException {
        var program = Files.readString(Paths.get("data/day5-input.txt")).trim();
        assertEquals(List.of(3508186L), executeAsDay5(program, 5L));
    }

    @Test
    void day9Example1() {
        var program = "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99";
        var machine = new Machine(program);
        var result = machine.run();
        assertTrue(machine.isHalted());
        String resultS = result.stream().map(Object::toString).collect(Collectors.joining(","));
        assertEquals(program, resultS);
    }

    @Test
    void day9Example2() {
        var program = "1102,34915192,34915192,7,4,7,99,0";
        var machine = new Machine(program);
        var result = machine.run();
        assertTrue(machine.isHalted());
        assertEquals(16, result.get(0).toString().length());
    }

    @Test
    void day9Example3() {
        var program = "104,1125899906842624,99";
        var machine = new Machine(program);
        var result = machine.run();
        assertTrue(machine.isHalted());
        String resultS = result.stream().map(Object::toString).collect(Collectors.joining(","));
        assertEquals("1125899906842624", resultS);
    }
}
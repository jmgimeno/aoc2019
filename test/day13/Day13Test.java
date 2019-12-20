package day13;

import day13.newmachine.Machine;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day13Test {

    static String executeAsDay2(String program) {
        var input = new LinkedBlockingQueue<BigInteger>();
        var output = new LinkedBlockingQueue<BigInteger>();
        var endSignaler = new CountDownLatch(1);
        var machine = new Machine(program, input, output, endSignaler);
        machine.run();
        return machine.dumpLowMemory();
    }

    @Test
    void day2tests() {
        assertEquals("2,0,0,0,99", executeAsDay2("1,0,0,0,99"));
        assertEquals("2,3,0,6,99", executeAsDay2("2,3,0,3,99"));
        assertEquals("2,4,4,5,99,9801", executeAsDay2("2,4,4,5,99,0"));
        assertEquals("30,1,1,4,2,5,6,0,99", executeAsDay2("1,1,1,4,99,5,6,0,99"));
    }

    static List<Integer> executeAsDay5(String program, int value) throws InterruptedException {
        var input = new LinkedBlockingQueue<BigInteger>();
        var output = new LinkedBlockingQueue<BigInteger>();
        var endSignaler = new CountDownLatch(1);
        input.add(BigInteger.valueOf(value));
        var machine = new Machine(program, input, output, endSignaler);
        machine.run();
        endSignaler.await();
        return output.stream()
                .map(BigInteger::intValue)
                .collect(Collectors.toUnmodifiableList());
    }

    @Test
    void day5part1() throws IOException, InterruptedException {
        var program = Files.readString(Paths.get("data/day5-input.txt")).trim();
        assertEquals(List.of(0, 0, 0, 0, 0, 0, 0, 0, 0, 12234644), executeAsDay5(program, 1));
    }

    @Test
    void day5Part2() throws IOException, InterruptedException {
        var program = Files.readString(Paths.get("data/day5-input.txt")).trim();
        assertEquals(List.of(3508186), executeAsDay5(program, 5));
    }

    @Test
    void day9Example1() {
        var program = "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99";
        var input = new LinkedBlockingQueue<BigInteger>();
        var output = new LinkedBlockingQueue<BigInteger>();
        var counter = new CountDownLatch(1);
        var machine = new Machine(program, input, output, counter);
        machine.run();
        String outputS = output.stream().map(Object::toString).collect(Collectors.joining(","));
        assertEquals(program, outputS);
    }

    @Test
    void day9Example2() {
        var program = "1102,34915192,34915192,7,4,7,99,0";
        var input = new LinkedBlockingQueue<BigInteger>();
        var output = new LinkedBlockingQueue<BigInteger>();
        var counter = new CountDownLatch(1);
        var machine = new Machine(program, input, output, counter);
        machine.run();
        assertEquals(16, output.peek().toString().length());
    }

    @Test
    void day9Example3() {
        var program = "104,1125899906842624,99";
        var input = new LinkedBlockingQueue<BigInteger>();
        var output = new LinkedBlockingQueue<BigInteger>();
        var counter = new CountDownLatch(1);
        var machine = new Machine(program, input, output, counter);
        machine.run();
        String outputS = output.stream().map(Object::toString).collect(Collectors.joining(","));
        assertEquals("1125899906842624", outputS);
    }
}
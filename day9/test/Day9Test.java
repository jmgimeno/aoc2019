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

class Day9Test {

    @Test
    void day5Part1() throws IOException, InterruptedException {
        var program = Files.readString(Paths.get("input-day5.txt")).trim();
        var input = new LinkedBlockingQueue<BigInteger>();
        var output = new LinkedBlockingQueue<BigInteger>();
        var counter = new CountDownLatch(1);
        input.put(BigInteger.valueOf(1));
        var machine = new Day9.ConcurrentMachine(program, input, output, counter);
        machine.run();
        var queueAsList = output.stream()
                .collect(Collectors.toUnmodifiableList());
        var expected = List.of(0, 0, 0, 0, 0, 0, 0, 0, 0, 12234644).stream()
                .map(BigInteger::valueOf)
                .collect(Collectors.toUnmodifiableList());
        assertEquals(expected, queueAsList);
    }

    @Test
    void day5Part2() throws IOException, InterruptedException {
        var program = Files.readString(Paths.get("input-day5.txt")).trim();
        var input = new LinkedBlockingQueue<BigInteger>();
        var output = new LinkedBlockingQueue<BigInteger>();
        var counter = new CountDownLatch(1);
        input.put(BigInteger.valueOf(5));
        var machine = new Day9.ConcurrentMachine(program, input, output, counter);
        machine.run();
        var queueAsList = output.stream().collect(Collectors.toUnmodifiableList());
        assertEquals(List.of(BigInteger.valueOf(3508186)), queueAsList);
    }

    @Test
    void example1() {
        var program = "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99";
        var input = new LinkedBlockingQueue<BigInteger>();
        var output = new LinkedBlockingQueue<BigInteger>();
        var counter = new CountDownLatch(1);
        var machine = new Day9.ConcurrentMachine(program, input, output, counter);
        machine.run();
        String outputS = output.stream().map(Object::toString).collect(Collectors.joining(","));
        assertEquals(program, outputS);
    }

    @Test
    void example2() {
        var program = "1102,34915192,34915192,7,4,7,99,0";
        var input = new LinkedBlockingQueue<BigInteger>();
        var output = new LinkedBlockingQueue<BigInteger>();
        var counter = new CountDownLatch(1);
        var machine = new Day9.ConcurrentMachine(program, input, output, counter);
        machine.run();
        assertEquals(16, output.peek().toString().length());
    }

    @Test
    void example3() {
        var program = "104,1125899906842624,99";
        var input = new LinkedBlockingQueue<BigInteger>();
        var output = new LinkedBlockingQueue<BigInteger>();
        var counter = new CountDownLatch(1);
        var machine = new Day9.ConcurrentMachine(program, input, output, counter);
        machine.run();
        String outputS = output.stream().map(Object::toString).collect(Collectors.joining(","));
        assertEquals("1125899906842624", outputS);
    }
}
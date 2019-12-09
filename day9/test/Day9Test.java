import org.junit.jupiter.api.Test;

import java.io.IOException;
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
        var input = new LinkedBlockingQueue<Integer>();
        var output = new LinkedBlockingQueue<Integer>();
        var counter = new CountDownLatch(1);
        input.put(1);
        var machine = new Day9.ConcurrentMachine(program, input, output, counter);
        machine.run();
        var queueAsList = output.stream().collect(Collectors.toUnmodifiableList());
        assertEquals(List.of(0, 0, 0, 0, 0, 0, 0, 0, 0, 12234644), queueAsList);
    }

    @Test
    void day5Part2() throws IOException, InterruptedException {
        var program = Files.readString(Paths.get("input-day5.txt")).trim();
        var input = new LinkedBlockingQueue<Integer>();
        var output = new LinkedBlockingQueue<Integer>();
        var counter = new CountDownLatch(1);
        input.put(5);
        var machine = new Day9.ConcurrentMachine(program, input, output, counter);
        machine.run();
        var queueAsList = output.stream().collect(Collectors.toUnmodifiableList());
        assertEquals(List.of(3508186), queueAsList);
    }
}
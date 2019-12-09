import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day9 {

    static class Memory {
        final List<BigInteger> low;
        final Map<BigInteger, BigInteger> high;
        BigInteger base;

        Memory(List<BigInteger> program) {
            low = program;
            high = new HashMap<>();
            base = BigInteger.ZERO;
        }

        private BigInteger innerGet(BigInteger pos) {
            if (pos.compareTo(BigInteger.valueOf(low.size())) < 0)
                return low.get(pos.intValue());
            else
                return high.getOrDefault(pos, BigInteger.ZERO);
        }

        private void innerSet(BigInteger pos, BigInteger value) {
            if (pos.compareTo(BigInteger.valueOf(low.size())) < 0)
                low.set(pos.intValue(), value);
            else
                high.put(pos, value);
        }

        BigInteger getImmediate(BigInteger position) {
            return innerGet(position);
        }

        BigInteger getPosition(BigInteger position) {
            return innerGet(innerGet(position));
        }

        BigInteger getRelative(BigInteger position) {
            return innerGet(base.add(innerGet(position)));
        }

        void moveBase(BigInteger increment) {
            base = base.add(increment);
        }

        public void setPosition(BigInteger pos, BigInteger value) {
            innerSet(innerGet(pos), value);
        }

        public void setImmediate(BigInteger pos, BigInteger value) {
            innerSet(pos, value);
        }

        public void setRelative(BigInteger pos, BigInteger value) {
            innerSet(base.add(innerGet(pos)), value);

        }
    }

    static class ConcurrentMachine implements Runnable {

        final Memory memory;
        final BlockingQueue<BigInteger> qInput;
        final BlockingQueue<BigInteger> qOutput;
        final CountDownLatch endSignal;

        BigInteger pc;
        boolean halted;

        enum Operation {ADD, MUL, INPUT, OUTPUT, JUMP_IF_TRUE, JUMP_IF_FALSE, LESS_THAN, EQUALS, ADJUST, HALT}
        enum Mode {POSITION, IMMEDIATE, RELATIVE}

        static Map<Integer, Operation> operationDecoder = Map.of(
                1, Operation.ADD,
                2, Operation.MUL,
                3, Operation.INPUT,
                4, Operation.OUTPUT,
                5, Operation.JUMP_IF_TRUE,
                6, Operation.JUMP_IF_FALSE,
                7, Operation.LESS_THAN,
                8, Operation.EQUALS,
                9, Operation.ADJUST,
                99, Operation.HALT
        );

        static Map<Integer, Mode> modeDecoder = Map.of(
                0, Mode.POSITION,
                1, Mode.IMMEDIATE,
                2, Mode.RELATIVE
        );

        ConcurrentMachine(String program, BlockingQueue<BigInteger> qInput, BlockingQueue<BigInteger> qOutput, CountDownLatch endSignal)  {
            this.memory = new Memory(
                    Stream.of(program.split(","))
                    .map(BigInteger::new)
                    .collect(Collectors.toCollection(ArrayList::new)));
            this.qInput = qInput;
            this.qOutput = qOutput;
            this.endSignal = endSignal;
        }

        @Override
        public void run() {
            try {
                pc = BigInteger.ZERO;
                Instruction instruction = new Instruction(memory.getImmediate(pc).intValue());
                while (!halted) {
                    instruction.execute();
                    instruction = new Instruction(memory.getImmediate(pc).intValue());
                }
                endSignal.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        class Instruction {

            final Operation operation;
            final List<Mode> accessors;

            Instruction(int opcode) {
                operation = operationDecoder.get(opcode % 100);
                accessors = IntStream
                        .iterate(opcode / 100, i -> i / 10)
                        .mapToObj(i -> modeDecoder.get(i % 10))
                        .limit(3)
                        .collect(Collectors.toUnmodifiableList());
            }

            @Override
            public String toString() {
                return "Instruction{" +
                        "operation=" + operation +
                        ", accessors=" + accessors +
                        '}';
            }

            BigInteger get(int i) {
                return switch(accessors.get(i - 1)) {
                    case POSITION -> memory.getPosition(PC_(i));
                    case IMMEDIATE -> memory.getImmediate(PC_(i));
                    case RELATIVE -> memory.getRelative(PC_(i));
                };
            }

            void set(int i, BigInteger value) {
                switch (accessors.get(i-1)) {
                    case POSITION -> memory.setPosition(PC_(i), value);
                    case IMMEDIATE -> memory.setImmediate(PC_(i), value);
                    case RELATIVE -> memory.setRelative(PC_(i), value);
                }
            }

            BigInteger PC_(int n) {
                return BigInteger.valueOf(n).add(pc);
            }

            private void execute() throws InterruptedException {
                switch (operation) {
                    case ADD -> {
                        set(3, get(1).add(get(2)));
                        pc = PC_(4);
                    }
                    case MUL -> {
                        set(3, get(1).multiply(get(2)));
                        pc = PC_(4);
                    }
                    case INPUT -> {
                        set(1, qInput.take());
                        pc = PC_(2);
                    }
                    case OUTPUT -> {
                        qOutput.put(get(1));
                        pc = PC_(2);
                    }
                    case JUMP_IF_TRUE -> {
                        if (!get(1).equals(BigInteger.ZERO)) {
                            pc = get(2);
                        } else {
                            pc = PC_(3);
                        }
                    }
                    case JUMP_IF_FALSE -> {
                        if (get(1).equals(BigInteger.ZERO)) {
                            pc = get(2);
                        } else {
                            pc = PC_(3);
                        }
                    }
                    case LESS_THAN -> {
                        set(3, get(1).compareTo(get(2)) < 0 ? BigInteger.ONE : BigInteger.ZERO);
                        pc = PC_(4);
                    }
                    case EQUALS -> {
                        set(3, get(1).equals(get(2)) ? BigInteger.ONE : BigInteger.ZERO);
                        pc = PC_(4);
                    }
                    case ADJUST -> {
                        memory.moveBase(get(1));
                        pc = PC_(2);
                    }
                    case HALT -> {
                        halted = true;
                    }
                }
            }
        }
    }

    static void part1() throws IOException, InterruptedException {
        var program = Files.readString(Paths.get("input.txt")).trim();
        var input = new LinkedBlockingQueue<BigInteger>();
        var output = new LinkedBlockingQueue<BigInteger>();
        var counter = new CountDownLatch(1);
        input.put(BigInteger.valueOf(1));
        var machine = new Day9.ConcurrentMachine(program, input, output, counter);
        machine.run();
        System.out.println("part1 = " + output);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        part1();
    }
}

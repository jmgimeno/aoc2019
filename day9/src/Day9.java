import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day9 {

    static class Memory {
        final List<BigInteger> low;
        final Map<BigInteger, BigInteger> high;

        Memory(List<BigInteger> program) {
            low = program;
            high = new HashMap<>();
        }

        BigInteger innerGet(BigInteger pos) {
            if (pos.compareTo(BigInteger.valueOf(low.size())) < 0)
                return low.get(pos.intValue());
            else
                return high.getOrDefault(pos, BigInteger.ZERO);
        }

        void innerSet(BigInteger pos, BigInteger value) {
            if (pos.compareTo(BigInteger.valueOf(low.size())) < 0)
                low.set(pos.intValue(), value);
            else
                high.put(pos, value);
        }

        BigInteger getImmediate(BigInteger position) {
            return innerGet(position);
        }

        BigInteger getPosition(BigInteger pos) {
            return innerGet(innerGet(pos));
        }

        void set(BigInteger pos, BigInteger value) {
            innerSet(innerGet(pos), value);
        }
    }

    static class ConcurrentMachine implements Runnable {

        final Memory memory;
        final BlockingQueue<BigInteger> qInput;
        final BlockingQueue<BigInteger> qOutput;
        final CountDownLatch endSignal;

        BigInteger pc;
        boolean halted;

        enum Operation {ADD, MUL, INPUT, OUTPUT, JUMP_IF_TRUE, JUMP_IF_FALSE, LESS_THAN, EQUALS, HALT}
        enum Mode {POSITION, IMMEDIATE}

        static Map<Integer, Operation> operationDecoder = Map.of(
                1, Operation.ADD,
                2, Operation.MUL,
                3, Operation.INPUT,
                4, Operation.OUTPUT,
                5, Operation.JUMP_IF_TRUE,
                6, Operation.JUMP_IF_FALSE,
                7, Operation.LESS_THAN,
                8, Operation.EQUALS,
                99, Operation.HALT
        );

        static Map<Integer, Mode> modeDecoder = Map.of(
                0, Mode.POSITION,
                1, Mode.IMMEDIATE
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

            BigInteger getArg(int i) {
                return switch(accessors.get(i - 1)) {
                    case POSITION -> memory.getPosition(PC(i));
                    case IMMEDIATE -> memory.getImmediate(PC(i));
                };
            }

            BigInteger PC(int n) {
                return BigInteger.valueOf(n).add(pc);
            }

            private void execute() throws InterruptedException {
                switch (operation) {
                    case ADD -> {
                        memory.set(PC(3), getArg(1).add(getArg(2)));
                        pc = PC(4);
                    }
                    case MUL -> {
                        memory.set(PC(3), getArg(1).multiply(getArg(2)));
                        pc = PC(4);
                    }
                    case INPUT -> {
                        var read = qInput.take();
                        memory.set(PC(1), read);
                        pc = PC(2);
                    }
                    case OUTPUT -> {
                        var write = getArg(1);
                        qOutput.put(write);
                        pc = PC(2);
                    }
                    case JUMP_IF_TRUE -> {
                        if (!getArg(1).equals(BigInteger.ZERO)) {
                            pc = getArg(2);
                        } else {
                            pc = PC(3);
                        }
                    }
                    case JUMP_IF_FALSE -> {
                        if (getArg(1).equals(BigInteger.ZERO)) {
                            pc = getArg(2);
                        } else {
                            pc = PC(3);
                        }
                    }
                    case LESS_THAN -> {
                        memory.set(PC(3), getArg(1).compareTo(getArg(2)) < 0 ? BigInteger.ONE : BigInteger.ZERO);
                        pc = PC(4);
                    }
                    case EQUALS -> {
                        memory.set(PC(3), getArg(1).equals(getArg(2)) ? BigInteger.ONE : BigInteger.ZERO);
                        pc = PC(4);
                    }
                    case HALT -> {
                        halted = true;
                    }
                }
            }
        }
    }
}

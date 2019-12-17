package day7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day7 {

    static class Permutations {

        // https://dzone.com/articles/java-8-master-permutations

        private Permutations() {
        }

        private static long factorial(int n) {
            if (n > 20 || n < 0) throw new IllegalArgumentException(n + " is out of range");
            return LongStream.rangeClosed(2, n).reduce(1, (a, b) -> a * b);
        }

        private static <T> List<T> permutation(long no, List<T> items) {
            return permutationHelper(no,
                    new LinkedList<>(Objects.requireNonNull(items)),
                    new ArrayList<>());
        }

        private static <T> List<T> permutationHelper(long no, LinkedList<T> in, List<T> out) {
            if (in.isEmpty()) return out;
            long subFactorial = factorial(in.size() - 1);
            out.add(in.remove((int) (no / subFactorial)));
            return permutationHelper((int) (no % subFactorial), in, out);
        }

        public static <T> Stream<List<T>> of(List<T> itemList) {
            return LongStream.range(0, factorial(itemList.size()))
                    .mapToObj(no -> permutation(no, itemList));
        }
    }

    static class ConcurrentMachine implements Runnable {

        List<Integer> state;
        final BlockingQueue<Integer> qInput;
        final BlockingQueue<Integer> qOutput;
        final CountDownLatch endSignal;
        int pc;
        boolean halted;

        enum Operation {ADD, MUL, INPUT, OUTPUT, JUMP_IF_TRUE, JUMP_IF_FALSE, LESS_THAN, EQUALS, HALT}

        static Map<Integer, Operation> operationDecoder = Map.of(
                1, Operation.ADD,
                2, Operation.MUL,
                3, Operation.INPUT,
                4, Operation.OUTPUT,
                5, Operation.JUMP_IF_TRUE,
                6, Operation.JUMP_IF_FALSE,
                7, Operation.LESS_THAN,
                8, Operation.EQUALS,
                99, Operation.HALT);

        Map<Integer, IntUnaryOperator> accessorDecoder = Map.of(
                0, pos -> state.get(state.get(pos)),
                1, pos -> state.get(pos)
        );

        ConcurrentMachine(String program, BlockingQueue<Integer> qInput, BlockingQueue<Integer> qOutput, CountDownLatch endSignal)  {
            this.state = Stream.of(program.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toCollection(ArrayList::new));
            this.qInput = qInput;
            this.qOutput = qOutput;
            this.endSignal = endSignal;
        }

        @Override
        public void run() {
            try {
                pc = 0;
                Instruction instruction = new Instruction(state.get(pc));
                while (!halted) {
                    instruction.execute();
                    instruction = new Instruction(state.get(pc));
                }
                endSignal.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        class Instruction {

            final Operation operation;
            final List<IntUnaryOperator> accessors;

            Instruction(int opcode) {
                operation = operationDecoder.get(opcode % 100);
                accessors = IntStream
                        .iterate(opcode / 100, i -> i / 10)
                        .mapToObj(i -> accessorDecoder.get(i % 10))
                        .limit(3)
                        .collect(Collectors.toUnmodifiableList());
            }

            private boolean isHalt() {
                return operation != Operation.HALT;
            }

            void set(int pos, int value) {
                state.set(state.get(pos), value);
            }

            int getArg(int i) {
                return accessors.get(i - 1).applyAsInt(pc + i);
            }

            private void execute() throws InterruptedException {
                switch (operation) {
                    case ADD -> {
                        set(pc + 3, getArg(1) + getArg(2));
                        pc += 4;
                    }
                    case MUL -> {
                        set(pc + 3, getArg(1) * getArg(2));
                        pc += 4;
                    }
                    case INPUT -> {
                        var read = qInput.take();
                        set(pc + 1, read);
                        pc += 2;
                    }
                    case OUTPUT -> {
                        var write = getArg(1);
                        qOutput.put(write);
                        pc += 2;
                    }
                    case JUMP_IF_TRUE -> {
                        if (getArg(1) != 0) {
                            pc = getArg(2);
                        } else {
                            pc += 3;
                        }
                    }
                    case JUMP_IF_FALSE -> {
                        if (getArg(1) == 0) {
                            pc = getArg(2);
                        } else {
                            pc += 3;
                        }
                    }
                    case LESS_THAN -> {
                        set(pc + 3, getArg(1) < getArg(2) ? 1 : 0);
                        pc += 4;
                    }
                    case EQUALS -> {
                        set(pc + 3, getArg(1) == getArg(2) ? 1 : 0);
                        pc += 4;
                    }
                    case HALT -> {
                        halted = true;
                    }
                }
            }
        }
    }

    static class ConcurrentAmplifiers {

        public static final int NUM_AMPLIFIERS = 5;

        final List<ConcurrentMachine> amplifiers;
        final List<BlockingQueue<Integer>> wires;
        final CountDownLatch endSignal;

        ConcurrentAmplifiers(String program) {
            endSignal = new CountDownLatch(NUM_AMPLIFIERS);
            wires = IntStream.range(0, NUM_AMPLIFIERS)
                    .mapToObj(dummy -> new LinkedBlockingQueue<Integer>())
                    .collect(Collectors.toUnmodifiableList());
            amplifiers = IntStream.range(0, NUM_AMPLIFIERS)
                    .mapToObj(i -> new ConcurrentMachine(
                            program,
                            wires.get(i % NUM_AMPLIFIERS),
                            wires.get((i + 1) % NUM_AMPLIFIERS),
                            endSignal))
                    .collect(Collectors.toUnmodifiableList());
        }

        int thrust(List<Integer> phaseSettings) {
            assert phaseSettings.size() == NUM_AMPLIFIERS;
            try {
                ExecutorService executor = Executors.newFixedThreadPool(NUM_AMPLIFIERS);
                amplifiers.forEach(executor::submit);
                for (int i = 0; i < NUM_AMPLIFIERS; i++) {
                    wires.get(i).put(phaseSettings.get(i));
                }
                wires.get(0).put(0);
                endSignal.await();
                executor.shutdown();
                return wires.get(0).take();
            } catch (InterruptedException e) {
                throw new IllegalStateException("Should not happen");
            }
        }

        private static int maxThrust(String program, List<Integer> phaseSettings) throws InterruptedException, ExecutionException {
            return Permutations.of(phaseSettings)
                    .mapToInt(settings -> new ConcurrentAmplifiers(program).thrust(settings))
                    .max()
                    .orElseThrow(() -> new IllegalStateException("Should not happen"));
        }

        public static int maxThrust1(String program) throws ExecutionException, InterruptedException {
            return maxThrust(program, List.of(0, 1, 2, 3, 4));
        }

        public static int maxThrust2(String program) throws ExecutionException, InterruptedException {
            return maxThrust(program, List.of(5, 6, 7, 8, 9));
        }
    }

    static void part1() throws IOException, ExecutionException, InterruptedException {
        var program = Files.readString(Paths.get("data/day7-day8-day9-day10-day11-day12-day13-day14-day15-day16-day17-input.txt")).trim();
        var part1 = ConcurrentAmplifiers.maxThrust1(program);
        System.out.println("part1 = " + part1);
    }

    static void part2() throws IOException, ExecutionException, InterruptedException {
        var program = Files.readString(Paths.get("data/day7-day8-day9-day10-day11-day12-day13-day14-day15-day16-day17-input.txt")).trim();
        var part2 = ConcurrentAmplifiers.maxThrust2(program);
        System.out.println("part2 = " + part2);
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        part1();
        part2();
    }
}

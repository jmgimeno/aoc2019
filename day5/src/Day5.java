import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day5 {

    static class Machine {

        List<Integer> state;
        Iterator<Integer> input;
        List<Integer> output;
        int pc;

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

        Map<Integer, Function<Integer, Integer>> accessorDecoder = Map.of(
                0, pos -> state.get(state.get(pos)),
                1, pos -> state.get(pos)
        );

        Machine(String program) {
            state = Stream.of(program.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        List<Integer> run(int value) {
            input = List.of(value).iterator();
            output = new ArrayList<>();
            pc = 0;
            Instruction instruction = new Instruction(state.get(pc));
            while (instruction.isHalt()) {
                instruction.execute();
                instruction = new Instruction(state.get(pc));
            }
            return output;
        }

        class Instruction {

            final Operation operation;
            final List<Function<Integer, Integer>> accessors;

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
                return accessors.get(i - 1).apply(pc + i);
            }

            private void execute() {
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
                        set(pc + 1, input.next());
                        pc += 2;
                    }
                    case OUTPUT -> {
                        output.add(getArg(1));
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
                    default -> throw new IllegalStateException("Bad op " + operation);
                }
            }
        }
    }

    static void part1() throws IOException {
        var program = Files.readString(Paths.get("input.txt")).trim();
        var machine = new Machine(program);
        var output = machine.run(1);
        System.out.println("part1 = " + output);
    }

    static void part2() throws IOException {
        var program = Files.readString(Paths.get("input.txt")).trim();
        var machine = new Machine(program);
        var output = machine.run(5);
        System.out.println("part2 = " + output);
    }

    public static void main(String[] args) throws IOException {
        part1();
        part2();
    }
}

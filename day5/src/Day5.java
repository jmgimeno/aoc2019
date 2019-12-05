import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day5 {

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
            99, Operation.HALT);

    static Map<Integer, Mode> modeDecoder = Map.of(
            0, Mode.POSITION,
            1, Mode.IMMEDIATE);

    static class Machine {

        List<Integer> state;
        Iterator<Integer> input;
        List<Integer> output;
        int pc;

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

        @Override
        public String toString() {
            return state.stream().map(Object::toString).collect(Collectors.joining(","));
        }

        class Instruction {
            final Operation operation;
            final Mode[] argModes;

            Instruction(int opcode) {
                operation = operationDecoder.get(opcode % 100);
                argModes = IntStream
                        .iterate(opcode / 100, i -> i / 10)
                        .mapToObj(i -> modeDecoder.get(i % 10))
                        .limit(3)
                        .toArray(Mode[]::new);
            }

            private boolean isHalt() {
                return operation != Operation.HALT;
            }

            int getPosition(int pos) {
                return state.get(state.get(pos));
            }

            void setPosition(int pos, int value) {
                state.set(state.get(pos), value);
            }

            int getImmediate(int pos) { return state.get(pos); }

            private void execute() {
                switch (operation) {
                    case ADD -> {
                        var arg1 = argModes[0] == Mode.POSITION ? getPosition(pc + 1) : getImmediate(pc + 1);
                        var arg2 = argModes[1] == Mode.POSITION ? getPosition(pc + 2) : getImmediate(pc + 2);
                        var result = arg1 + arg2;
                        assert argModes[2] == Mode.POSITION;
                        setPosition(pc + 3, result);
                        pc += 4;
                    }
                    case MUL -> {
                        var arg1 = argModes[0] == Mode.POSITION ? getPosition(pc + 1) : getImmediate(pc + 1);
                        var arg2 = argModes[1] == Mode.POSITION ? getPosition(pc + 2) : getImmediate(pc + 2);
                        var result = arg1 * arg2;
                        assert argModes[2] == Mode.POSITION;
                        setPosition(pc + 3, result);
                        pc += 4;
                    }
                    case INPUT -> {
                        var value = input.next();
                        assert argModes[0] == Mode.POSITION;
                        setPosition(pc + 1, value);
                        pc += 2;
                    }
                    case OUTPUT -> {
                        var arg1 = argModes[0] == Mode.POSITION ? getPosition(pc + 1) : getImmediate(pc + 1);
                        output.add(arg1);
                        pc += 2;
                    }
                    case JUMP_IF_TRUE -> {
                        var arg1 = argModes[0] == Mode.POSITION ? getPosition(pc + 1) : getImmediate(pc + 1);
                        if (arg1 != 0) {
                            pc = argModes[1] == Mode.POSITION ? getPosition(pc + 2) : getImmediate(pc + 2);
                        } else {
                            pc += 3;
                        }
                    }
                    case JUMP_IF_FALSE -> {
                        var arg1 = argModes[0] == Mode.POSITION ? getPosition(pc + 1) : getImmediate(pc + 1);
                        if (arg1 == 0) {
                            pc = argModes[1] == Mode.POSITION ? getPosition(pc + 2) : getImmediate(pc + 2);
                        } else {
                            pc += 3;
                        }
                    }
                    case LESS_THAN -> {
                        var arg1 = argModes[0] == Mode.POSITION ? getPosition(pc + 1) : getImmediate(pc + 1);
                        var arg2 = argModes[1] == Mode.POSITION ? getPosition(pc + 2) : getImmediate(pc + 2);
                        var result = arg1 < arg2 ? 1 : 0;
                        assert argModes[2] == Mode.POSITION;
                        setPosition(pc + 3, result);
                        pc += 4;
                    }
                    case EQUALS -> {
                        var arg1 = argModes[0] == Mode.POSITION ? getPosition(pc + 1) : getImmediate(pc + 1);
                        var arg2 = argModes[1] == Mode.POSITION ? getPosition(pc + 2) : getImmediate(pc + 2);
                        var result = arg1 == arg2 ? 1 : 0;
                        assert argModes[2] == Mode.POSITION;
                        setPosition(pc + 3, result);
                        pc += 4;
                    }
                    default -> throw new IllegalStateException("Bad op " + operation);
                };
            }
        }
    }

    static String execute(String initial) {
        var machine = new Machine(initial);
        machine.run(0);
        return machine.toString();
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day5 {

    enum Operation {ADD, MUL, LOAD, STORE, HALT}

    enum Mode {POSITION, IMMEDIATE}

    static Map<Integer, Operation> operationDecoder = Map.of(
            1, Operation.ADD,
            2, Operation.MUL,
            3, Operation.LOAD,
            4, Operation.STORE,
            99, Operation.HALT);

    static Map<Operation, Integer> operationLength = Map.of(
            Operation.ADD, 4,
            Operation.MUL, 4,
            Operation.LOAD, 2,
            Operation.STORE, 2,
            Operation.HALT, 1);

    static Map<Integer, Mode> modeDecoder = Map.of(
            0, Mode.POSITION,
            1, Mode.IMMEDIATE);

    static class Instruction {
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
    }

    static class Machine {

        List<Integer> state;

        Machine(String initial) {
            state = Stream.of(initial.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        int get(int pos) {
            return state.get(state.get(pos));
        }

        void set(int pos, int value) {
            state.set(state.get(pos), value);
        }

        void run() {
            int pc = 0;
            Instruction instruction = new Instruction(state.get(pc));
            while (instruction.operation != Operation.HALT) {
                var arg1 = get(pc + 1);
                var arg2 = get(pc + 2);
                var result = switch (instruction.operation) {
                    case ADD -> arg1 + arg2;
                    case MUL -> arg1 * arg2;
                    default -> throw new IllegalStateException("Bad op " + instruction.operation);
                };
                set(pc + 3, result);
                pc += operationLength.get(instruction.operation);
                instruction = new Instruction(state.get(pc));
            }
        }

        void restore1202() {
            setNounAndVerb(12, 2);
        }

        void setNounAndVerb(int noun, int verb) {
            state.set(1, noun);
            state.set(2, verb);
        }

        @Override
        public String toString() {
            return state.stream().map(Object::toString).collect(Collectors.joining(","));
        }
    }

    static String execute(String initial) {
        var machine = new Machine(initial);
        machine.run();
        return machine.toString();
    }

    static void part1() throws IOException {
        var program = Files.readString(Paths.get("input-day2.txt")).trim();
        var machine = new Machine(program);
        machine.restore1202();
        machine.run();
        System.out.println("part1 = " + machine.state.get(0));
    }

    static class IntTriple {
        final int result;
        final int noun;
        final int verb;

        public IntTriple(int result, int noun, int verb) {
            this.result = result;
            this.noun = noun;
            this.verb = verb;
        }
    }

    static IntTriple run(String program, int noun, int verb) {
        var machine = new Machine(program);
        machine.setNounAndVerb(noun, verb);
        machine.run();
        return new IntTriple(machine.state.get(0), noun, verb);
    }

    static void part2() throws IOException {
        var program = Files.readString(Paths.get("input-day2.txt")).trim();
        var part2 = IntStream.range(0, 100)
                .boxed()
                .flatMap(noun -> IntStream.range(0, 100).mapToObj(verb -> run(program, noun, verb)))
                .dropWhile(r -> r.result != 19690720)
                .map(r -> 100 * r.noun + r.verb)
                .findFirst()
                .orElseThrow();
        System.out.printf("part2 = %d%n", part2);
    }

    public static void main(String[] args) throws IOException {
        part1();
        part2();
    }
}

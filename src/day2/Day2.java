package day2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day2 {

    static class Machine {

        static final int ADD = 1;
        static final int MUL = 2;
        static final int HALT = 99;

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
            int op = state.get(pc);
            while (op != HALT) {
                var arg1 = get(pc + 1);
                var arg2 = get(pc + 2);
                var result = switch (op) {
                    case ADD -> arg1 + arg2;
                    case MUL -> arg1 * arg2;
                    default -> throw new IllegalStateException("Bad op " + op);
                };
                set(pc + 3, result);
                pc += 4;
                op = state.get(pc);
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
        var program = Files.readString(Paths.get("data/day2-input.txt")).trim();
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
        var program = Files.readString(Paths.get("data/day2-input.txt")).trim();
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

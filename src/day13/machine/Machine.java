package day13.machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Machine {

    final Memory memory;

    Long input;
    List<Long> output;
    long pc;
    boolean halted;
    boolean waiting;

    public String dumpLowMemory() {
        return memory.dumpLowMemory();
    }

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

    public Machine(
            String program) {

        this.memory = new Memory(
                Stream.of(program.split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toCollection(ArrayList::new)));
        this.pc = 0L;
    }

    public List<Long> run() {
        output = new ArrayList<>();
        Instruction instruction = new Instruction((int) memory.getImmediate(pc));
        while (!halted && !waiting) {
            instruction.execute();
            instruction = new Instruction((int) memory.getImmediate(pc));
        }
        return output;
    }

    public void addInput(long value) {
        input = value;
        waiting = false;
    }

    public void poke(int address, int value) {
        memory.setImmediate(address, value);
    }

    public boolean isHalted() {
        return halted;
    }

    public boolean isWaiting() {
        return waiting;
    }

    class Instruction {

        final Operation operation;
        final List<Mode> accessors;

        Instruction(int opCode) {
            operation = operationDecoder.get(opCode % 100);
            accessors = IntStream
                    .iterate(opCode / 100, i -> i / 10)
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

        long get(int i) {
            return switch (accessors.get(i - 1)) {
                case POSITION -> memory.getPosition(PC_(i));
                case IMMEDIATE -> memory.getImmediate(PC_(i));
                case RELATIVE -> memory.getRelative(PC_(i));
            };
        }

        void set(int i, long value) {
            switch (accessors.get(i - 1)) {
                case POSITION -> memory.setPosition(PC_(i), value);
                case IMMEDIATE -> memory.setImmediate(PC_(i), value);
                case RELATIVE -> memory.setRelative(PC_(i), value);
            }
        }

        long PC_(int n) {
            return pc + n;
        }

        private void execute() {
            switch (operation) {
                case ADD -> {
                    set(3, get(1) + get(2));
                    pc = PC_(4);
                }
                case MUL -> {
                    set(3, get(1) * get(2));
                    pc = PC_(4);
                }
                case INPUT -> {
                    if (!waiting && input == null) {
                        waiting = true;
                    } else {
                        waiting = false;
                        set(1, input);
                        input = null;
                        pc = PC_(2);
                    }
                }
                case OUTPUT -> {
                    output.add(get(1));
                    pc = PC_(2);
                }
                case JUMP_IF_TRUE -> {
                    if (get(1) != 0L) {
                        pc = get(2);
                    } else {
                        pc = PC_(3);
                    }
                }
                case JUMP_IF_FALSE -> {
                    if (get(1) == 0L) {
                        pc = get(2);
                    } else {
                        pc = PC_(3);
                    }
                }
                case LESS_THAN -> {
                    set(3, get(1) < get(2) ? 1L : 0L);
                    pc = PC_(4);
                }
                case EQUALS -> {
                    set(3, get(1) == get(2) ? 1L : 0L);
                    pc = PC_(4);
                }
                case ADJUST -> {
                    memory.moveBase(get(1));
                    pc = PC_(2);
                }
                case HALT -> halted = true;
            }
        }
    }
}


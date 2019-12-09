import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Memory {
    final List<Integer> low;

    Memory(List<Integer> program) {
        this.low = program;
    }

    int getPosition(int pos) {
        return low.get(low.get(pos));
    }

    int getImmediate(int position) {
        return low.get(position);
    }

    void set(int pos, int value) {
        low.set(pos, value);
    }
}

class ConcurrentMachine implements Runnable {

    final Memory memory;
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

    ConcurrentMachine(String program, BlockingQueue<Integer> qInput, BlockingQueue<Integer> qOutput, CountDownLatch endSignal)  {
        var bytecode = Stream.of(program.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toCollection(ArrayList::new));
        this.memory = new Memory(bytecode);
        this.qInput = qInput;
        this.qOutput = qOutput;
        this.endSignal = endSignal;
    }

    @Override
    public void run() {
        try {
            pc = 0;
            Instruction instruction = new Instruction(memory.getImmediate(pc));
            while (!halted) {
                instruction.execute();
                instruction = new Instruction(memory.getImmediate(pc));
            }
            endSignal.countDown();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    class Instruction {

        final Operation operation;
        final int[] accessors;

        Instruction(int opcode) {
            operation = operationDecoder.get(opcode % 100);
            accessors = IntStream
                    .iterate(opcode / 100, i -> i / 10)
                    .map(i -> i % 10)
                    .limit(3)
                    .toArray();
        }

        int getArg(int i) {
            return switch (accessors[i]) {
                case 0 -> memory.getPosition(pc + i);
                case 1 -> memory.getImmediate(pc + i);
                default -> throw new IllegalStateException("Should not happen");
            };
        }

        private void execute() throws InterruptedException {
            switch (operation) {
                case ADD -> {
                    memory.set(pc + 3, getArg(1) + getArg(2));
                    pc += 4;
                }
                case MUL -> {
                    memory.set(pc + 3, getArg(1) * getArg(2));
                    pc += 4;
                }
                case INPUT -> {
                    var read = qInput.take();
                    memory.set(pc + 1, read);
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
                    memory.set(pc + 3, getArg(1) < getArg(2) ? 1 : 0);
                    pc += 4;
                }
                case EQUALS -> {
                    memory.set(pc + 3, getArg(1) == getArg(2) ? 1 : 0);
                    pc += 4;
                }
                case HALT -> {
                    halted = true;
                }
            }
        }
    }
}

public class Day9 {
}

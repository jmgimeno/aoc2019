import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class ConcurrentMachine implements Runnable {

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

    public ConcurrentMachine(
            String program,
            BlockingQueue<BigInteger> qInput,
            BlockingQueue<BigInteger> qOutput) {

        this(program, qInput, qOutput, null);
    }

    ConcurrentMachine(
            String program,
            BlockingQueue<BigInteger> qInput,
            BlockingQueue<BigInteger> qOutput,
            CountDownLatch endSignal) {

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
            if (endSignal != null) endSignal.countDown();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

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

class XY {
    final int x;
    final int y;

    static XY xy(int x, int y) {
        return new XY(x, y);
    }

    XY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XY xy = (XY) o;
        return x == xy.x &&
                y == xy.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public XY step(Orientation orientation) {
        return null;
    }
}

enum Orientation {R, D, L, U;

    public Orientation turn(Turn turn) {
        return null;
    }
};

enum Turn {LEFT, RIGHT};
enum Color {WHITE, BLACK}

class Grid {

    Map<XY, Color> panels;

    Color get(XY xy) {
        return panels.getOrDefault(xy, Color.BLACK);
    }

    public void paint(XY position, Color color) {

    }
}

class Robot {

    final ExecutorService executorService;
    final BlockingQueue<BigInteger> brainInput;
    final BlockingQueue<BigInteger> brainOutput;
    final CountDownLatch finishSignal;
    final ConcurrentMachine brain;

    public Robot(String program) {
        executorService = Executors.newSingleThreadExecutor();
        brainInput = new LinkedBlockingQueue<>();
        brainOutput = new LinkedBlockingQueue<>();
        finishSignal = new CountDownLatch(1);
        brain = new ConcurrentMachine(program, brainInput, brainOutput, finishSignal);
        executorService.submit(brain);
    }

    Grid paintHull() throws InterruptedException {
        var grid = new Grid();
        var position = XY.xy(0, 0);
        var orientation = Orientation.U;
        do {
            var color = grid.get(position);
            brainInput.put(color == Color.BLACK ? BigInteger.ZERO : BigInteger.ONE);
            var paintTo = brainOutput.take().intValue() == 0 ? Color.BLACK : Color.WHITE;
            var turn = brainOutput.take().intValue() == 0 ? Turn.LEFT : Turn.RIGHT;
            grid.paint(position, paintTo);
            orientation = orientation.turn(turn);
            position = position.step(orientation);
        } while (finishSignal.getCount() != 0);
        return grid;
    }
}

public class Day11 {

    static void part1() throws IOException {
        var program = Files.readString(Paths.get("input.txt")).trim();
    }

    public static void main(String[] args) throws IOException {
        part1();
    }
}

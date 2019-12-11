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
            return switch (accessors.get(i - 1)) {
                case POSITION -> memory.getPosition(PC_(i));
                case IMMEDIATE -> memory.getImmediate(PC_(i));
                case RELATIVE -> memory.getRelative(PC_(i));
            };
        }

        void set(int i, BigInteger value) {
            switch (accessors.get(i - 1)) {
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

class Position {
    final int x;
    final int y;

    static Position xy(int x, int y) {
        return new Position(x, y);
    }

    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Position step(Orientation direction) {
        return switch(direction) {
            case RIGHT -> xy(x+1, y);
            case DOWN  -> xy(x, y+1);
            case LEFT  -> xy(x-1, y);
            case UP    -> xy(x, y-1);
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position xy = (Position) o;
        return x == xy.x &&
                y == xy.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

enum Orientation {
    RIGHT {
        @Override
        public Orientation turn(Turn turn) {
            return switch (turn) {
                case LEFT -> UP;
                case RIGHT -> DOWN;
            };
        }
    },
    DOWN {
        @Override
        public Orientation turn(Turn turn) {
            return switch (turn) {
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
            };
        }
    },
    LEFT {
        @Override
        public Orientation turn(Turn turn) {
            return switch (turn) {
                case LEFT -> DOWN;
                case RIGHT -> UP;
            };
        }
    },
    UP {
        @Override
        public Orientation turn(Turn turn) {
            return switch (turn) {
                case LEFT -> LEFT;
                case RIGHT -> RIGHT;
            };
        }
    };

    abstract Orientation turn(Turn turn);
};

enum Turn {LEFT, RIGHT};

enum Color {WHITE, BLACK}

class Grid {

    Map<Position, Color> panels = new HashMap<>();

    Color get(Position position) {
        return panels.getOrDefault(position, Color.BLACK);
    }

    public void paint(Position position, Color color) {
        panels.put(position, color);
    }

    public int numPainted() {
        return panels.size();
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

    void paintHull(Grid grid) throws InterruptedException {
        var position = Position.xy(0, 0);
        var orientation = Orientation.UP;
        do {
            var color = grid.get(position);
            brainInput.put(color == Color.BLACK ? BigInteger.ZERO : BigInteger.ONE);
            var paintTo = brainOutput.take().intValue() == 0 ? Color.BLACK : Color.WHITE;
            var turn = brainOutput.take().intValue() == 0 ? Turn.LEFT : Turn.RIGHT;
            grid.paint(position, paintTo);
            orientation = orientation.turn(turn);
            position = position.step(orientation);
        } while (finishSignal.getCount() != 0);
        executorService.shutdown();
    }
}

public class Day11 {

    static void part1() throws IOException, InterruptedException {
        var program = Files.readString(Paths.get("input.txt")).trim();
        var robot = new Robot(program);
        var grid = new Grid();
        robot.paintHull(grid);
        System.out.println("part1 = " + grid.numPainted());
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        part1();
    }
}

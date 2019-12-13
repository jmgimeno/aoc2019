package robot;

import machine.ConcurrentMachine;

import java.math.BigInteger;
import java.util.concurrent.*;

public class Robot {

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

    public void paintHull(Grid grid) throws InterruptedException {
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

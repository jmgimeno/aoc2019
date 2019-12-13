package arcade;

import machine.ConcurrentMachine;

import java.math.BigInteger;
import java.util.concurrent.*;

public class Arcade {

    final ExecutorService executorService;
    final BlockingQueue<BigInteger> brainInput;
    final BlockingQueue<BigInteger> brainOutput;
    final CountDownLatch finishSignal;
    final ConcurrentMachine brain;

    public Arcade(String program) {
        executorService = Executors.newSingleThreadExecutor();
        brainInput = new LinkedBlockingQueue<>();
        brainOutput = new LinkedBlockingQueue<>();
        finishSignal = new CountDownLatch(1);
        brain = new ConcurrentMachine(program, brainInput, brainOutput, finishSignal);
        executorService.submit(brain);
    }

    public void play(Screen screen) throws InterruptedException {
        do {
            var x = brainOutput.take().intValue();
            var y = brainOutput.take().intValue();
            var tileId = TileId.fromInt(brainOutput.take().intValue());
            var position = new Position(x, y);
            screen.draw(position, tileId);
        } while (finishSignal.getCount() != 0);
        executorService.shutdown();
    }
}

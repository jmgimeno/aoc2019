package arcade;

import machine.ConcurrentMachine;

import java.math.BigInteger;
import java.util.concurrent.*;

public class Arcade {

    final static int FRAME_SIZE = 20 * 44;

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
            var info = brainOutput.take().intValue();
            if (x == -1 && y == 0)
                screen.updateScore(info);
            else
                screen.draw(new Position(x, y), TileId.fromInt(info));
            System.out.println(screen.render());
        } while (finishSignal.getCount() != 0);
        executorService.shutdown();
    }

    public void insertCoins() {
        brain.poke(0, 2);  // A little APPLE ][ love
    }
}

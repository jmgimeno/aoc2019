package arcade;

import machine.ConcurrentMachine;

import java.math.BigInteger;
import java.util.concurrent.*;

public class Arcade {

    final ExecutorService executorService;
    final JoyStick brainInput;
    final BlockingQueue<BigInteger> brainOutput;
    final CountDownLatch finishSignal;
    final ConcurrentMachine brain;

    public Arcade(String program) {
        executorService = Executors.newFixedThreadPool(5);
        brainInput = new JoyStick();
        brainOutput = new LinkedBlockingQueue<>();
        finishSignal = new CountDownLatch(1);
        brain = new ConcurrentMachine(program, brainInput, brainOutput, finishSignal);
    }

    public void play(Screen screen) throws InterruptedException {
        executorService.submit(brain);
        int xBall = -1, xPaddle = -1;
        do {
            var x = brainOutput.take().intValue();
            var y = brainOutput.take().intValue();
            var info = brainOutput.take().intValue();
            if (x == -1 && y == 0) {
                System.out.println("Received score");
                screen.updateScore(info);
                System.out.println(screen.render());
            } else {

                var tileId = TileId.fromInt(info);
                screen.draw(new Position(x, y), tileId);
                if (tileId == TileId.BALL) {
                    xBall = x;
                    System.out.println("xBall = " + xBall);
                    if (xBall != -1 && xPaddle != -1) {
                        var joyStick = Integer.signum(xBall - xPaddle);
                        System.out.println("joyStick = " + joyStick);
                        brainInput.put(joyStick);
                    }
                } else if (tileId == TileId.HORIZONTAL_PADDLE) {
                    xPaddle = x;
                    System.out.println("xPaddle = " + xPaddle);
                    if (xBall != -1 && xPaddle != -1) {
                        var joyStick = Integer.signum(xBall - xPaddle);
                        System.out.println("joyStick = " + joyStick);
                        brainInput.put(joyStick);
                    }
                } else {
                    System.out.println("Received tile");
                }
            }
        } while (finishSignal.getCount() != 0);
        executorService.shutdown();
    }

    public void insertCoins() {
        brain.poke(0, 2);  // A little APPLE ][ love
    }
}

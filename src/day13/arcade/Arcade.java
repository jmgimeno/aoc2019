package day13.arcade;

import day13.machine.Machine;

public class Arcade {

    final Machine brain;
    int score;

    public Arcade(String program) {
        brain = new Machine(program);
        score = 0;
    }

    public void play(Screen screen) {
        int xBall = 20, xPaddle = 20;
        do {
            var output = brain.run();
            while (!output.isEmpty()) {
                long x = output.remove(0);
                long y = output.remove(0);
                long info = output.remove(0);
                if (x == -1L && y == 0L) {
                    score = (int) info;
                    screen.updateScore((int) info);
                } else {
                    var tileId = TileId.fromInt((int) info);
                    screen.draw(Position.xy((int) x, (int) y), tileId);
                    if (tileId == TileId.BALL) {
                        xBall = (int) x;
                    } else if (tileId == TileId.PADDLE) {
                        xPaddle = (int) x;
                    }
                }
            }
            if (brain.isWaiting()) {
                var input = Integer.compare(xBall, xPaddle);
                brain.addInput(input);
            }
        } while (!brain.isHalted());
    }

    public void insertCoins() {
        brain.poke(0, 2);  // A little APPLE ][ love
    }

    public int getScore() {
        return score;
    }
}

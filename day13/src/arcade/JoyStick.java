package arcade;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

public class JoyStick {

    private final AtomicInteger value = new AtomicInteger(0);

    public synchronized void put(int newValue) {
        System.out.println("Joy stick updated to " + newValue);
        value.set(newValue);
    }

    public synchronized BigInteger take() {
        System.out.println("Joy stick read " + value);
        return BigInteger.valueOf(value.get());
    }
}

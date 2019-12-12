import java.util.List;
import java.util.stream.IntStream;

public class Cluster {
    final List<Moon> moons;

    public Cluster(List<Moon> moons) {
        this.moons = moons;
    }

    public void step() {
        moons.forEach(moon -> {
            moon.updateVelocity(moons);
        });
        moons.forEach(Moon::updatePosition);
    }

    public void step(int n) {
        for (int i = 1; i <= n; i++) step();
    }

    public int totalEnergy() {
        return moons.stream()
                .mapToInt(Moon::totalEnergy)
                .sum();
    }

    @Override
    public String toString() {
        return "System{" +
                "moons=" + moons +
                '}';
    }
}

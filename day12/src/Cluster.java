import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Cluster {
    final List<Moon> moons;

    public Cluster(List<Moon> moons) {
        this.moons = moons;
    }

    public Cluster step() {
        var newMoons = moons.stream()
                .map(moon -> moon.step(moons))
                .collect(Collectors.toUnmodifiableList());
        return new Cluster(newMoons);
    }

    public Cluster step(int n) {
        var cluster = this;
        for (int i = 1; i <= n; i++)
            cluster = cluster.step();
        return cluster;
    }

    public int totalEnergy() {
        return moons.stream()
                .mapToInt(Moon::totalEnergy)
                .sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cluster cluster = (Cluster) o;
        return moons.equals(cluster.moons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moons);
    }

    @Override
    public String toString() {
        return "System{" +
                "moons=" + moons +
                '}';
    }
}

package day12;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Cluster<V extends Vector<V>> {
    final List<Moon<V>> moons;

    public Cluster(List<Moon<V>> moons) {
        this.moons = moons;
    }

    public Cluster<V> step() {
        var newMoons = moons.stream()
                .map(moon -> moon.step(moons))
                .collect(Collectors.toUnmodifiableList());
        return new Cluster<>(newMoons);
    }

    public Cluster<V> step(int n) {
        var cluster = this;
        for (int i = 1; i <= n; i++)
            cluster = cluster.step();
        return cluster;
    }

    public long stepsToZeroVelocity() {
        var steps = 0L;
        var cluster = this;
        do {
            cluster = cluster.step();
            steps += 1L;
        } while (!cluster.allStopped());
        return steps;
    }

    private boolean allStopped() {
        return moons.stream()
                .allMatch(Moon::isStopped);
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
        Cluster<?> cluster = (Cluster<?>) o;
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

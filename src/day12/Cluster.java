package day12;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

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
        return Stream.iterate(this, Cluster::step)
                .skip(n)
                .findFirst()
                .orElseThrow();
    }

    public long stepsToZeroVelocity() {
        return Stream.iterate(
                    step(),
                    not(Cluster::allStopped),
                    Cluster::step)
                .count() + 1;
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

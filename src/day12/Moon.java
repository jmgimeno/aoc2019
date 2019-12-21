package day12;

import java.util.List;
import java.util.Objects;

public class Moon<V extends Vector<V>> {
    final V position, velocity;

    public Moon(V position) {
        this.position = position;
        this.velocity = position.zero();
    }

    public Moon(V position, V velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    public V getPosition() {
        return position;
    }

    public Moon<V> step(List<Moon<V>> others) {
        V newVelocity = others.stream()
                .map(other -> position.diff(other.position))
                .reduce(position.zero(), Vector::add)
                .add(velocity);
        var newPosition = position.add(newVelocity);
        return new Moon<>(newPosition, newVelocity);
    }

    public boolean isStopped() {
        return velocity.isZero();
    }

    public int potentialEnergy() {
        return position.abs();
    }

    public int kineticEnergy() {
        return velocity.abs();
    }

    public int totalEnergy() {
        return potentialEnergy() * kineticEnergy();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Moon<?> moon = (Moon<?>) o;
        return position.equals(moon.position) &&
                velocity.equals(moon.velocity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, velocity);
    }

    @Override
    public String toString() {
        return "day12.Moon{" +
                "position=" + position +
                ", velocity=" + velocity +
                '}';
    }
}

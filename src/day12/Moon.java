package day12;

import java.util.List;
import java.util.Objects;

public class Moon {
    final Vector position, velocity;

    public Moon(Vector position) {
        this.position = position;
        this.velocity = new Vector();
    }

    public Moon(Vector position, Vector velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    public Moon step(List<Moon> others) {
        var newVelocity = others.stream()
                .map(other -> position.diff(other.position))
                .reduce(new Vector(), Vector::add)
                .add(velocity);
        var newPosition = position.add(newVelocity);
        return new Moon(newPosition, newVelocity);
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
        Moon moon = (Moon) o;
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

import java.util.List;

public class Moon {
    Vector position;
    Vector velocity;

    public Moon(Vector position) {
        this.position = position;
        this.velocity = new Vector();
    }

    public void updatePosition() {
        position = position.add(velocity);
    }

    public void updateVelocity(List<Moon> others) {
        velocity = others.stream()
                .map(other -> position.diff(other.position))
                .reduce(new Vector(), Vector::add)
                .add(velocity);
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
    public String toString() {
        return "Moon{" +
                "position=" + position +
                ", velocity=" + velocity +
                '}';
    }
}

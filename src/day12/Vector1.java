package day12;

import java.util.Objects;

import static java.lang.Integer.signum;

public class Vector1 implements Vector<Vector1> {

    private final int x;

    public Vector1(int x) {
        this.x = x;
    }

    @Override
    public Vector1 zero() {
        return new Vector1(0);
    }

    @Override
    public Vector1 add(Vector1 other) {
        return new Vector1(x + other.x);
    }

    @Override
    public Vector1 diff(Vector1 other) {
        return new Vector1(signum(other.x - x));
    }

    @Override
    public int abs() {
        return Math.abs(x);
    }

    @Override
    public boolean isZero() {
        return x == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector1 vector1 = (Vector1) o;
        return x == vector1.x;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x);
    }

    @Override
    public String toString() {
        return "Vector1{" +
                "x=" + x +
                '}';
    }
}

package day12;

import java.util.Objects;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.signum;

public class Vector3 implements Vector<Vector3> {

    static Pattern pattern = Pattern.compile("<x=(-?\\d+), y=(-?\\d+), z=(-?\\d+)>");

    public static Vector3 parseVector(String str) {
        var matcher = pattern.matcher(str);
        matcher.matches();
        return new Vector3(
                parseInt(matcher.group(1)),
                parseInt(matcher.group(2)),
                parseInt(matcher.group(3)));
    }

    private final int x, y, z;

    public Vector3(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector1 getX() {
        return new Vector1(x);
    }

    public Vector1 getY() {
        return new Vector1(y);
    }

    public Vector1 getZ() {
        return new Vector1(z);
    }

    @Override
    public Vector3 zero() {
        return new Vector3(0, 0, 0);
    }

    @Override
    public Vector3 add(Vector3 other) {
        return new Vector3(
                x + other.x,
                y + other.y,
                z + other.z);
    }

    @Override
    public Vector3 diff(Vector3 other) {
        return new Vector3(
                signum(other.x - x),
                signum(other.y - y),
                signum(other.z - z));
    }

    @Override
    public int abs() {
        return Math.abs(x) + Math.abs(y) + Math.abs(z);
    }

    @Override
    public boolean isZero() {
        return x == 0 && y == 0 && z == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3 vector3 = (Vector3) o;
        return x == vector3.x &&
                y == vector3.y &&
                z == vector3.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "day12.Vector{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}


import java.net.InterfaceAddress;
import java.util.Objects;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.signum;

public class Vector {

    static Pattern pattern = Pattern.compile("<x=(-?\\d+), y=(-?\\d+), z=(-?\\d+)>");

    final int x, y, z;

    public Vector() {
        this(0, 0, 0);
    }

    public Vector(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector add(Vector other) {
        return new Vector(
                x + other.x,
                y + other.y,
                z + other.z);
    }

    public Vector diff(Vector other) {
        return new Vector(
                signum(other.x - x),
                signum(other.y - y),
                signum(other.z - z));
    }

    public int abs() {
        return Math.abs(x) + Math.abs(y) + Math.abs(z);
    }

    public static Vector parseVector(String str) {
        var matcher = pattern.matcher(str);
        matcher.matches();
        return new Vector(
                parseInt(matcher.group(1)),
                parseInt(matcher.group(2)),
                parseInt(matcher.group(3)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return x == vector.x &&
                y == vector.y &&
                z == vector.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}


package day12;

public interface Vector<V extends Vector<V>> {
    V zero();
    V add(V other);
    V diff(V other);
    int abs();
    boolean isZero();
}

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day4Test {

    @Test
    void digits() {
        assertArrayEquals(new int[]{1,2,3,4,5,6}, Day4.digits(123456));
        assertArrayEquals(new int[]{0,0,3,4,5,6}, Day4.digits(3456));
    }

    @Test
    void neverDecreasing() {
        assertTrue(Day4.neverDecreasing(Day4.digits(111111)));
        assertFalse(Day4.neverDecreasing(Day4.digits(223450)));
        assertTrue(Day4.neverDecreasing(Day4.digits(123789)));
    }

    @Test
    void adjacent() {
        assertTrue(Day4.adjacent(Day4.digits(111111)));
        assertTrue(Day4.adjacent(Day4.digits(223450)));
        assertFalse(Day4.adjacent(Day4.digits(123789)));
    }

    @Test
    void isValid() {
        assertTrue(Day4.isValid(111111));
        assertFalse(Day4.isValid(223450));
        assertFalse(Day4.isValid(123789));
    }

    @Test
    void adjacent2() {
        assertTrue(Day4.adjacent2(Day4.digits(112233)));
        assertFalse(Day4.adjacent2(Day4.digits(123444)));
        assertTrue(Day4.adjacent2(Day4.digits(111122)));
    }
}
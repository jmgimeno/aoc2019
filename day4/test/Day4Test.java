import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day4Test {

    @Test
    void digits2() {
        assertArrayEquals(new int[]{6,5,4,3,2,1}, Day4.revDigits(123456));
        assertArrayEquals(new int[]{6,5,4,3,0,0}, Day4.revDigits(3456));
    }

    @Test
    void neverDecreasing() {
        assertTrue(Day4.neverDecreasing(Day4.revDigits(111111)));
        assertFalse(Day4.neverDecreasing(Day4.revDigits(223450)));
        assertTrue(Day4.neverDecreasing(Day4.revDigits(123789)));
    }

    @Test
    void adjacent() {
        assertTrue(Day4.adjacent(Day4.revDigits(111111)));
        assertTrue(Day4.adjacent(Day4.revDigits(223450)));
        assertFalse(Day4.adjacent(Day4.revDigits(123789)));
    }

    @Test
    void isValid() {
        assertTrue(Day4.isValid(111111));
        assertFalse(Day4.isValid(223450));
        assertFalse(Day4.isValid(123789));
    }

    @Test
    void adjacent2() {
        assertTrue(Day4.adjacent2(Day4.revDigits(112233)));
        assertFalse(Day4.adjacent2(Day4.revDigits(123444)));
        assertTrue(Day4.adjacent2(Day4.revDigits(111122)));
    }
}
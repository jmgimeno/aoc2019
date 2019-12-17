package day16;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FFTTest {

    @Test
    void phase1() {
        var fft = FFT.phase("12345678");
        assertArrayEquals(new int[]{4, 8, 2, 2, 6, 1, 5, 8}, fft);
    }

    @Test
    void phase2() {
        var fft = FFT.phase("12345678", 2);
        assertArrayEquals(new int[]{3, 4, 0, 4, 0, 4, 3, 8}, fft);
    }

    @Test
    void phase3() {
        var fft = FFT.phase("12345678", 3);
        assertArrayEquals(new int[]{0, 3, 4, 1, 5, 5, 1, 8}, fft);
    }

    @Test
    void phase4() {
        var fft = FFT.phase("12345678", 4);
        assertArrayEquals(new int[]{0, 1, 0, 2, 9, 4, 9, 8}, fft);
    }

    @Test
    void firstEight1() {
        assertEquals("24176176", FFT.firstEight("80871224585914546619083218645595", 100));
    }

    @Test
    void firstEight2() {
        assertEquals("73745418", FFT.firstEight("19617804207202209144916044189917", 100));
    }

    @Test
    void firstEight3() {
        assertEquals("52432133", FFT.firstEight("69317163492948606335995924319873", 100));
    }
}
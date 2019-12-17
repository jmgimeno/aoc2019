package day6;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day6Test {
    @Test
    void totalOrbits() throws IOException {
        assertEquals(42L, Day6.totalOrbits("data/day6-test1.txt"));
    }

    @Test
    void totalTransfers() throws IOException {
        assertEquals(4L, Day6.totalTransfers("data/day6-test2.txt"));
    }
}
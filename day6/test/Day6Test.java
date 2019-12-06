import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class Day6Test {
    @Test
    void totalOrbits() throws IOException {
        assertEquals(42L, Day6.totalOrbits("test1.txt"));
    }

    @Test
    void totalTransfers() throws IOException {
        assertEquals(4L, Day6.totalTransfers("test2.txt"));
    }
}
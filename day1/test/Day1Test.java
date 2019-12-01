import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day1Test {

    @Test
    void requiredFuel() {
        assertEquals(2, Day1.requiredFuel(12));
        assertEquals(2, Day1.requiredFuel(14));
        assertEquals(654, Day1.requiredFuel(1969));
        assertEquals(33583, Day1.requiredFuel(100756));
    }

    @Test
    void totalFuel() {
        assertEquals(2, Day1.totalFuel(14));
        assertEquals(966, Day1.totalFuel(1969));
        assertEquals(50346, Day1.totalFuel(100756));
    }
}
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day5Test {

    @Test
    void execute() {
        assertEquals("2,0,0,0,99", Day5.execute("1,0,0,0,99"));
        assertEquals("2,3,0,6,99", Day5.execute("2,3,0,3,99"));
        assertEquals("2,4,4,5,99,9801", Day5.execute("2,4,4,5,99,0"));
        assertEquals("30,1,1,4,2,5,6,0,99", Day5.execute("1,1,1,4,99,5,6,0,99"));
    }
}
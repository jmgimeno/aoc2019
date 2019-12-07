import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Day7Test {

    public static final String PROGRAM1 = "3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0";
    public static final String PROGRAM2 = "3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0";
    public static final String PROGRAM3 = "3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0";

    @Test
    void thrust1() {
        Day7.Amplifiers amplifiers = new Day7.Amplifiers(PROGRAM1);
        assertEquals(43210, amplifiers.thrust(List.of(4,3,2,1,0)));
    }

    @Test
    void thrust2() {
        Day7.Amplifiers amplifiers = new Day7.Amplifiers(PROGRAM2);
        assertEquals(54321, amplifiers.thrust(List.of(0,1,2,3,4)));
    }

    @Test
    void thrust3() {
        Day7.Amplifiers amplifiers = new Day7.Amplifiers(PROGRAM3);
        assertEquals(65210, amplifiers.thrust(List.of(1,0,4,3,2)));
    }

    @Test
    void maxThrust1() {
        assertEquals(43210, Day7.Amplifiers.maxThrust(PROGRAM1));
    }

    @Test
    void maxThrust2() {
        assertEquals(54321, Day7.Amplifiers.maxThrust(PROGRAM2));
    }

    @Test
    void maxThrust3() {
        assertEquals(65210, Day7.Amplifiers.maxThrust(PROGRAM3));
    }
}
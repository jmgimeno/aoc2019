package day8;

import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class Day8Test {

    Day8.Image image = new Day8.Image(3, 2, "123456789012");

    @Test
    void layers() {
        var layer1 = image.getLayer(1).mapToObj(Integer::toString).collect(Collectors.joining());
        var layer2 = image.getLayer(2).mapToObj(Integer::toString).collect(Collectors.joining());
        assertEquals("123456", layer1);
        assertEquals("789012", layer2);
    }
}
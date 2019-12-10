import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day10Test {

    @Test
    void example1() {
        var map =
                """
                .#..#
                .....
                #####
                ....#
                ...##
                """;
        var region = new Day10.Region(map);
        assertEquals(8, region.maxDetected());
        //assertEquals(new Day10.Point(3, 4), region.maxPoint());
    }

    @Test
    void example2() {
        var map = """
                ......#.#.
                #..#.#....
                ..#######.
                .#.#.###..
                .#..#.....
                ..#....#.#
                #..#....#.
                .##.#..###
                ##...#..#.
                .#....####
                """;
        var region = new Day10.Region(map);
        assertEquals(33, region.maxDetected());
        //assertEquals(new Day10.Point(5, 8), region.maxPoint());
    }

    @Test
    void example3() {
        var map = """
                #.#...#.#.
                .###....#.
                .#....#...
                ##.#.#.#.#
                ....#.#.#.
                .##..###.#
                ..#...##..
                ..##....##
                ......#...
                .####.###.
                """;
        var region = new Day10.Region(map);
        assertEquals(35, region.maxDetected());
        //assertEquals(new Day10.Point(1, 2), region.maxPoint());
    }

    @Test
    void example4() {
        var map = """
                .#..#..###
                ####.###.#
                ....###.#.
                ..###.##.#
                ##.##.#.#.
                ....###..#
                ..#.#..#.#
                #..#.#.###
                .##...##.#
                .....#.#..
                """;
        var region = new Day10.Region(map);
        assertEquals(41, region.maxDetected());
        //assertEquals(new Day10.Point(6, 3), region.maxPoint());
    }

    @Test
    void example5() {
        var map = """
                .#..##.###...#######
                ##.############..##.
                .#.######.########.#
                .###.#######.####.#.
                #####.##.#.##.###.##
                ..#####..#.#########
                ####################
                #.####....###.#.#.##
                ##.#################
                #####.##.###..####..
                ..######..##.#######
                ####.##.####...##..#
                .#####..#.######.###
                ##...#.##########...
                #.##########.#######
                .####.#.###.###.#.##
                ....##.##.###..#####
                .#.#.###########.###
                #.#.#.#####.####.###
                ###.##.####.##.#..##
                """;
        var region = new Day10.Region(map);
        assertEquals(210, region.maxDetected());
        //assertEquals(new Day10.Point(11, 13), region.maxPoint());
    }

}
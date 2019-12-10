import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Day10Test {

    @Test
    void part1Ex1() {
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
        assertEquals(new Day10.Point(3, 4), region.maxPoint());
    }

    @Test
    void part1Ex2() {
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
        assertEquals(new Day10.Point(5, 8), region.maxPoint());
    }

    @Test
    void part1Ex3() {
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
        assertEquals(new Day10.Point(1, 2), region.maxPoint());
    }

    @Test
    void part1Ex4() {
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
        assertEquals(new Day10.Point(6, 3), region.maxPoint());
    }

    @Test
    void part1Ex5() {
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
        assertEquals(new Day10.Point(11, 13), region.maxPoint());
    }

    @Test
    void part2Ex1() {
        var map = """
                .#....#####...#..
                ##...##.#####..##
                ##...#...#.#####.
                ..#.....X...###..
                ..#.#.....#....##
                """;
        var region = new Day10.Region(map);
        var laser = Day10.findLaser(map);
        assertEquals(new Day10.Point(8, 3), laser);
        var targets = region.findTargets(laser, 9);
        assertEquals(new Day10.Point(8, 1), targets.get(0));
        assertEquals(new Day10.Point(9, 0), targets.get(1));
        assertEquals(new Day10.Point(9, 1), targets.get(2));
        assertEquals(new Day10.Point(10, 0), targets.get(3));
        assertEquals(new Day10.Point(9, 2), targets.get(4));
        assertEquals(new Day10.Point(11, 1), targets.get(5));
        assertEquals(new Day10.Point(12, 1), targets.get(6));
        assertEquals(new Day10.Point(11, 2), targets.get(7));
        assertEquals(new Day10.Point(15, 1), targets.get(8));
    }

    @Test
    void part2Ex2() {
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
        var laser = new Day10.Point(11, 13);
        var targets = region.findTargets(laser, 500);
        assertEquals(299, targets.size());
        assertEquals(new Day10.Point(11, 12), targets.get(0));
        assertEquals(new Day10.Point(12, 1), targets.get(1));
        assertEquals(new Day10.Point(12, 2), targets.get(2));
        assertEquals(new Day10.Point(12, 8), targets.get(9));
        assertEquals(new Day10.Point(16, 0), targets.get(19));
        assertEquals(new Day10.Point(16, 9), targets.get(49));
        assertEquals(new Day10.Point(10, 16), targets.get(99));
        assertEquals(new Day10.Point(9, 6), targets.get(198));
        assertEquals(new Day10.Point(8, 2), targets.get(199));
        assertEquals(new Day10.Point(10, 9), targets.get(200));
        assertEquals(new Day10.Point(11, 1), targets.get(298));
    }
}
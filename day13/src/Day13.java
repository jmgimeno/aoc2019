import robot.Color;
import robot.Grid;
import robot.Position;
import robot.Robot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day13 {

    static void part1() throws IOException, InterruptedException {
        var program = Files.readString(Paths.get("input.txt")).trim();
        var robot = new Robot(program);
        var grid = new Grid();
        robot.paintHull(grid);
        System.out.println("part1 = " + grid.numPainted());
    }

    static void part2() throws IOException, InterruptedException {
        var program = Files.readString(Paths.get("input.txt")).trim();
        var robot = new Robot(program);
        var grid = new Grid();
        grid.paint(Position.xy(0,0), Color.WHITE);
        robot.paintHull(grid);
        System.out.println("part2");
        System.out.println(grid.render());
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        part1();
        part2();
    }

}

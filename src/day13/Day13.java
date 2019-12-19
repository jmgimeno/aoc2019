package day13;

import day13.arcade.Arcade;
import day13.arcade.Screen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day13 {

    static void part1() throws IOException, InterruptedException {
        var program = Files.readString(Paths.get("data/day13-day14-day15-day16-day17-input.txt")).trim();
        var arcade = new Arcade(program);
        var screen = new Screen();
        arcade.play(screen);
        System.out.println("part1 = " + screen.numBlocks());
    }

    static void part2() throws IOException, InterruptedException {
        var program = Files.readString(Paths.get("data/day13-day14-day15-day16-day17-input.txt")).trim();
        var arcade = new Arcade(program);
        var screen = new Screen();
        arcade.insertCoins();
        arcade.play(screen);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        part1();
        //part2();
    }

}
package day13;

import day13.arcade.Arcade;
import day13.arcade.Screen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day13 {

    static void part1() throws IOException {
        var program = Files.readString(Paths.get("data/day13-input.txt")).trim();
        var arcade = new Arcade(program);
        var screen = new Screen();
        arcade.play(screen);
        System.out.println("part1 = " + screen.numBlocks());
    }

    static void part2() throws IOException {
        var program = Files.readString(Paths.get("data/day13-input.txt")).trim();
        var arcade = new Arcade(program);
        var screen = new Screen();
        arcade.insertCoins();
        arcade.play(screen);
        System.out.println(screen.render());
        var part2 = arcade.getScore();
        System.out.println("part2 = " + part2);
    }

    public static void main(String[] args) throws IOException {
        part1();
        part2();
    }

}

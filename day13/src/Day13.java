import arcade.Arcade;
import arcade.Screen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day13 {

    static void part1() throws IOException, InterruptedException {
        var program = Files.readString(Paths.get("input.txt")).trim();
        var arcade = new Arcade(program);
        var screen = new Screen();
        arcade.play(screen);
        System.out.println("part1 = " + screen.numBlocks());
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        part1();
    }

}

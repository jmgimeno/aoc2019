import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.IntStream;

public class Day8 {

    static final int WIDE = 25;
    static final int TALL = 6;

    static class Image {
        private final int wide;
        private final int tall;
        private final int[] pixels;

        Image(int wide, int tall, String str) {
            this.wide = wide;
            this.tall = tall;
            this.pixels = str.chars().map(c -> c - '0').toArray();
        }

        int numLayers() {
            return pixels.length / (wide * tall);
        }

        IntStream getLayer(int numLayer) {
            var begin = (numLayer - 1) * wide * tall;
            var end = begin + wide * tall;
            return IntStream.range(begin, end).map(i -> pixels[i]);
        }

        int countValue(int numLayer, int value) {
            return (int) getLayer(numLayer)
                    .filter(p -> p == value)
                    .count();
        }

        int layerOfMinZeros() {
            return IntStream
                    .rangeClosed(1, numLayers())
                    .boxed()
                    .min(Comparator.comparingInt(l -> countValue(l, 0)))
                    .orElseThrow();
        }
    }

    static void part1() throws IOException {
        var imageString = Files.readString(Paths.get("input.txt"));
        var image = new Image(WIDE, TALL, imageString);
        var zeroesLayer = image.layerOfMinZeros();
        var ones = image.countValue(zeroesLayer, 1);
        var twos = image.countValue(zeroesLayer, 2);
        System.out.println("part1 = " + ones * twos);
    }

    public static void main(String[] args) throws IOException {
        part1();
    }
}

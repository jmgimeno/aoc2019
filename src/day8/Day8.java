package day8;

import javax.sound.midi.SysexMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day8 {

    static final int WIDE = 25;
    static final int TALL = 6;

    static final int BLACK = 0;
    static final int WHITE = 1;
    static final int TRANSPARENT = 2;

    static class Layer {
        final int[] pixels;

        public Layer(int size) {
            this.pixels = IntStream
                    .generate(() -> TRANSPARENT)
                    .limit(size).toArray();
        }

        public Layer(int[] pixels) {
            this.pixels = pixels;
        }

        public void mergeWith(Layer down) {
            IntStream.range(0, pixels.length)
                    .forEach(i -> {
                        if (pixels[i] == TRANSPARENT)
                            pixels[i] = down.pixels[i];
                    });
        }

        public IntStream asStream() {
            return Arrays.stream(pixels);
        }

        public void render(int wide) {
            String str = asStream().mapToObj(i -> i == BLACK ? " " : "\u25a0")
                    .collect(Collectors.joining());
            IntStream.range(0, pixels.length / wide)
                    .mapToObj(r -> str.substring(r * wide, (r + 1) * wide))
                    .forEach(System.out::println);
        }
    }

    static class Image {
        private final int wide;
        private final int tall;
        private final List<Layer> layers;

        Image(int wide, int tall, String str) {
            this.wide = wide;
            this.tall = tall;
            this.layers = createLayers(str, wide * tall);
        }

        private List<Layer> createLayers(String str, int size) {
            var numLayers = str.length() / size;
            return IntStream.range(0, numLayers)
                    .mapToObj(l -> str.substring(l * size, (l + 1) * size))
                    .map(strLayer -> strLayer.chars().map(c -> c - '0').toArray())
                    .map(Layer::new)
                    .collect(Collectors.toUnmodifiableList());
        }

        int numLayers() {
            return layers.size();
        }

        IntStream getLayer(int numLayer) {
            return layers.get(numLayer - 1).asStream();
        }

        int countValue(int numLayer, int value) {
            return (int) getLayer(numLayer)
                    .filter(p -> p == value)
                    .count();
        }

        int layerOfMinZeros() {
            return IntStream.rangeClosed(1, numLayers())
                    .boxed()
                    .min(Comparator.comparingInt(l -> countValue(l, 0)))
                    .orElseThrow();
        }

        public Layer decode() {
            var result = new Layer(wide * tall);
            layers.forEach(result::mergeWith);
            return result;
        }
    }

    static void part1() throws IOException {
        var imageString = Files.readString(Paths.get("data/day8-day9-day10-day11-day12-day13-day14-day15-day16-day17-input.txt")).trim();
        var image = new Image(WIDE, TALL, imageString);
        var zeroesLayer = image.layerOfMinZeros();
        var ones = image.countValue(zeroesLayer, 1);
        var twos = image.countValue(zeroesLayer, 2);
        System.out.println("part1 = " + ones * twos);
    }

    static void part2() throws IOException {
        var imageString = Files.readString(Paths.get("data/day8-day9-day10-day11-day12-day13-day14-day15-day16-day17-input.txt")).trim();
        var image = new Image(WIDE, TALL, imageString);
        var decoded = image.decode();
        System.out.println("part2");
        decoded.render(WIDE);
    }

    public static void main(String[] args) throws IOException {
        part1();
        part2();
    }
}
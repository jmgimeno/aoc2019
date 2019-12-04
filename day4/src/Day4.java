import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Day4 {

    static int[] digits(int n) {
        var digits = new int[6];
        for (int i = 5; i >= 0; i--) {
            digits[i] = n % 10;
            n /= 10;
        }
        return digits;
    }

    static boolean neverDecreasing(int[] digits) {
        return IntStream
                .range(0, digits.length - 1)
                .noneMatch(i -> digits[i] > digits[i + 1]);
    }

    static boolean adjacent(int[] digits) {
        return IntStream
                .range(0, digits.length - 1)
                .anyMatch(i -> digits[i] == digits[i + 1]);
    }

    static boolean isValid(int n) {
        var digits = digits(n);
        return adjacent(digits) && neverDecreasing(digits);
    }

    static long howMany(int from, int to) {
        return IntStream.range(from, to + 1).filter(Day4::isValid).count();
    }

    static void part1() {
        long part1 = howMany(123257, 647015);
        System.out.println("part1 = " + part1);
    }

    static boolean isValid2(int n) {
        var digits = digits(n);
        return adjacent2(digits) && neverDecreasing(digits);
    }

    static boolean adjacent2(int[] digits) {
        return IntStream
                .range(0, digits.length - 1)
                .anyMatch(i -> switch (i) {
                    case 0 -> digits[0] == digits[1] && digits[1] != digits[2];
                    case 4 -> digits[3] != digits[4] && digits[4] == digits[5];
                    default -> digits[i - 1] != digits[i] && digits[i] == digits[i + 1] && digits[i + 1] != digits[i + 2];
                });
    }

    static long howMany2(int from, int to) {
        return IntStream.range(from, to + 1).filter(Day4::isValid2).count();
    }

    static void part2() {
        long part2 = howMany2(123257, 647015);
        System.out.println("part2 = " + part2);
    }

    public static void main(String[] args) {
        part1();
        part2();
    }
}

package day4;

import java.util.stream.IntStream;

public class Day4 {

    static int[] revDigits(int n) {
        // Least significant on position 0
        return IntStream
                .iterate(n, d -> d / 10)
                .map(d -> d % 10)
                .limit(6)
                .toArray();
    }

    static boolean neverDecreasing(int[] revDigits) {
        return IntStream
                .range(0, revDigits.length - 1)
                .noneMatch(i -> revDigits[i + 1] > revDigits[i]);
    }

    static boolean adjacent(int[] revDigits) {
        return IntStream
                .range(0, revDigits.length - 1)
                .anyMatch(i -> revDigits[i] == revDigits[i + 1]);
    }

    static boolean isValid(int n) {
        var revDigits = revDigits(n);
        return adjacent(revDigits) && neverDecreasing(revDigits);
    }

    static long howMany(int from, int to) {
        return IntStream
                .rangeClosed(from, to)
                .filter(Day4::isValid)
                .count();
    }

    static void part1() {
        long part1 = howMany(123257, 647015);
        System.out.println("part1 = " + part1);
    }

    static boolean isValid2(int n) {
        var revDigits = revDigits(n);
        return adjacent2(revDigits) && neverDecreasing(revDigits);
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
        return IntStream
                .rangeClosed(from, to)
                .filter(Day4::isValid2)
                .count();
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

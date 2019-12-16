import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FFT {

    public static int[] phase(String str) {
        return phase(str.chars().map(c -> c - '0').toArray());
    }

    public static int[] phase(int[] digits) {
        int[] phase = new int[digits.length];
        for (int n= 0; n < phase.length; n++) {
            int plus = 0;
            for (int i = n; i < digits.length; i += 4 * (n + 1)) {
                for (int j = 0; j <= n && (i + j) < digits.length; j++) {
                    plus += digits[i + j];
                }
            }
            int minus = 0;
            for (int i = n + 2 * (n + 1); i < digits.length; i += 4 * (n + 1)) {
                for (int j = 0; j <= n && (i + j) < digits.length; j++) {
                    minus += digits[i + j];
                }
            }
            phase[n] = Math.abs(plus - minus) % 10;
        }
        return phase;
    }

    public static int[] phase(String str, int n) {
        return phase(str.chars().map(c -> c - '0').toArray(), n);
    }

    public static int[] phase(int[] digits, int n) {
        int[] result = Arrays.copyOf(digits, digits.length);
        for (int i = 1; i <= n; i++) {
            result = phase(result);
        }
        return result;
    }

    public static String firstEight(String str, int n) {
        return Arrays.stream(phase(str, n))
                .limit(8)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }
}

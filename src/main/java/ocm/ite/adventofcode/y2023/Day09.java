package ocm.ite.adventofcode.y2023;

import ocm.ite.adventofcode.AocUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day09 {

    public static void main(String[] args) {
        part1();
        part2();
    }

    public static void part1() {
        long result = 0;

        for (var line : AocUtils.readLines("/input/2023/day09.txt")) {
            var values = Arrays.stream(line.split(" "))
                    .filter(Predicate.not(String::isBlank))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            var interpolations = new ArrayList<Long>();
            int i = 1;
            boolean stop;
            do {
                interpolations.add(calculateForDepth(values, i, values.size() - 1));
                i++;
                var idx = values.size() - 1;
                stop = interpolations.get(interpolations.size() - 1) == 0;
                while (stop && idx > i) {
                    idx--;
                    stop = calculateForDepth(values, i, idx) == 0;
                }
            } while (!stop);

            result += interpolations.stream().reduce(Long::sum).orElse(0L) + values.get(values.size() - 1);
        }

        System.out.println(result);
    }

    public static void part2() {
        long result = 0;

        for (var line : AocUtils.readLines("/input/2023/day09.txt")) {
            var values = Arrays.stream(line.split(" "))
                    .filter(Predicate.not(String::isBlank))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            var interpolations = new ArrayList<Long>();
            int i = 1;
            boolean stop;
            do {
                interpolations.add(calculateForDepth(values, i, values.size() - 1));
                i++;
                var idx = values.size() - 1;
                stop = interpolations.get(interpolations.size() - 1) == 0;
                while (stop && idx > i) {
                    idx--;
                    stop = calculateForDepth(values, i, idx) == 0;
                }
            } while (!stop);
            long prev = 0;
            for (int j = i - 1; j > 0; j--) {
                prev = calculateForDepth(values, j, j) - prev;
            }
            result += values.get(0) - prev;
        }

        System.out.println(result);
    }

    private static Long calculateForDepth(List<Long> values, int k, int startIdx) {
        var result = values.get(startIdx);
        for (int i = 1; i <= k; i++) {
            result += nCr(k, i) * (i % 2 == 0 ? 1 : -1) * values.get(startIdx - i);
        }
        return result;
    }

    public static int nCr(long n, long r) {
        if (r > n)
            return 0;
        if (r == 0 || r == n)
            return 1;
        return nCr(n - 1, r - 1) + nCr(n - 1, r);
    }
}

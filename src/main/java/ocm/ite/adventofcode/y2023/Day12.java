package ocm.ite.adventofcode.y2023;

import ocm.ite.adventofcode.AocUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day12 {

    public static void main(String[] args) {
        part1();
        part2();
    }

    public static void part1() {
        long result = 0;
        for (var line : AocUtils.readLines("/input/2023/day12.txt")) {
            var entry = line.split(" ");
            var record = entry[0];
            var groupSizes = Arrays.stream(entry[1].split(","))
                    .map(Integer::parseInt)
                    .toList();

            var arrangements = countArrangements(record, groupSizes);
            result += arrangements;
        }
        System.out.println(result);
    }

    public static void part2() {
        long result = 0;
        var lines = AocUtils.readLines("/input/2023/day12.txt");
        for (var line : lines) {
            var entry = line.split(" ");
            var actualRecord = entry[0];
            var records = ((actualRecord + "?").repeat(4) + actualRecord);
            var groupSizes = Arrays.stream(entry[1].split(","))
                    .map(Integer::parseInt)
                    .toList();

            var gs = Collections.nCopies(5, groupSizes)
                    .stream()
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

            long arrangements = countArrangements(records, gs);
            result += arrangements;
        }
        System.out.println(result);
    }

    private static long countArrangements(String records, List<Integer> gs) {
        char[] charArray = records.toCharArray();
        Integer[] sizes = gs.toArray(Integer[]::new);

        Long[][][] cache = new Long[2][charArray.length][10000];

        return countArrangements(charArray, sizes, 0, 0, '.', 0, cache);
    }


    private static long countArrangements(char[] charArray, Integer[] groupSizes, int pos, int gsIdx, char prev, int sharpCount, Long[][][] cache) {
        long arrangements = 0;
        if (pos == charArray.length) {
            return groupSizes[groupSizes.length - 1] == 0 ? 1 : 0;
        }
        var idx = prev == '.' ? 0 : 1;
        if (cache[idx][pos][sharpCount] != null) {
            return cache[idx][pos][sharpCount];
        }
        if (charArray[pos] == '?') {
            // count as #
            if (gsIdx < groupSizes.length && groupSizes[gsIdx] > 0) {
                groupSizes[gsIdx]--;
                charArray[pos] = '#';
                arrangements += countArrangements(charArray, groupSizes, pos + 1, gsIdx, '#', sharpCount + 1, cache);
                groupSizes[gsIdx]++;
                charArray[pos] = '?';
            }
            // count as .
            if (!(gsIdx < groupSizes.length && prev == '#' && groupSizes[gsIdx] > 0)) {
                charArray[pos] = '.';
                arrangements += countArrangements(charArray, groupSizes, pos + 1, prev == '#' ? gsIdx + 1 : gsIdx, '.', sharpCount, cache);
                charArray[pos] = '?';
            }
        } else if (charArray[pos] == '.') {
            if (gsIdx < groupSizes.length && groupSizes[gsIdx] != 0 && prev == '#') {
                return 0;
            } else {
                arrangements += countArrangements(charArray, groupSizes, pos + 1, prev == '#' ? gsIdx + 1 : gsIdx, '.', sharpCount, cache);
            }
        } else {
            if (gsIdx < groupSizes.length && groupSizes[gsIdx] > 0) {
                groupSizes[gsIdx]--;
                arrangements += countArrangements(charArray, groupSizes, pos + 1, gsIdx, '#', sharpCount + 1, cache);
                groupSizes[gsIdx]++;
            } else {
                return 0;
            }
        }
        cache[idx][pos][sharpCount] = arrangements;
        return arrangements;
    }

}

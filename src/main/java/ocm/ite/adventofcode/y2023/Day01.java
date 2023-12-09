package ocm.ite.adventofcode.y2023;

import ocm.ite.adventofcode.AocUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day01 {

    private static final Map<String, Integer> digits = Map.of(
            "one", 1,
            "two", 2,
            "three", 3,
            "four", 4,
            "five", 5,
            "six", 6,
            "seven", 7,
            "eight", 8,
            "nine", 9
    );

    public static void main(String[] args) {
        part1();
        part2();
    }

    public static void part1() {
        var lines = AocUtils.readLines("/input/2023/day01.txt");
        Long first;
        Long last;
        int result = 0;
        Long digit = 0L;
        for (var line : lines) {
            first = null;
            last = null;

            for (Character c : line.toCharArray()) {
                var isDigit = c <= '9' && c >= '0';
                if (isDigit) {
                    digit = (long) c - '0';
                    if (first == null) {
                        first = digit;
                    }
                    last = digit;
                }
            }
            long tempSum = first * 10 + last;
            result += tempSum;
        }
        System.out.println(result);
    }


    public static void part2() {
        var hashMap = digits.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> hashLetter(e.getKey()),
                        e -> (long) e.getValue()
                ));

        var lines = AocUtils.readLines("/input/2023/day01.txt");
        Long first;
        Long last;
        int result = 0;
        Long digit = 0L;
        var last5 = new LinkedList<Character>();
        for (var line : lines) {
            first = null;
            last = null;

            for (Character c : line.toCharArray()) {
                if (last5.size() == 5) {
                    last5.removeFirst();
                }
                last5.addLast(c);
                var isDigit = c <= '9' && c >= '0';
                if (isDigit) {
                    digit = (long) c - '0';
                }
                if (!isDigit) {
                    digit = extractDigit(last5, hashMap);
                    isDigit = digit != null;
                }
                if (isDigit) {
                    if (first == null) {
                        first = digit;
                    }
                    last = digit;
                }
            }
            long tempSum = first * 10 + last;
            result += tempSum;
        }
        System.out.println(result);
    }

    private static Long extractDigit(List<Character> key, Map<Long, Long> hashMap) {
        long hash = 0;
        Long result;
        int multiplier = 1;
        for (int i = 0; i < key.size(); i++) {
            hash += multiplier * (key.get(key.size() - i - 1) - 'a');
            multiplier *= 100;
            result = hashMap.get(hash);
            if (result != null) {
                return result;
            }
        }
        return null;
    }


    private static Long hashLetter(String key) {
        long hash = 0;
        for (char c : key.toCharArray()) {
            hash *= 100;
            hash += (c - 'a');
        }
        return hash;
    }

}

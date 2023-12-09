package ocm.ite.adventofcode.y2023;

import ocm.ite.adventofcode.AocUtils;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day07 {

    public static void main(String[] args) {
        part1();
        part2();
    }

    public static void part1() {
        long result = 1;
        var lines = AocUtils.readLines("/input/2023/day06.txt");

        var times = Arrays.stream(lines.get(0).substring("Time:".length()).split(" "))
                .filter(Predicate.not(String::isBlank))
                .map(Long::parseLong)
                .toList();

        var distances = Arrays.stream(lines.get(1).substring("Distance:".length()).split(" "))
                .filter(Predicate.not(String::isBlank))
                .map(Long::parseLong)
                .toList();
        for (int i =0; i < times.size(); i++) {
            var time = times.get(i);
            var distance = distances.get(i);

            var delta = time * time - 4 * distance;
            var tMin = (int) Math.floor(((double) time - Math.sqrt(delta)) / 2);
            var tMax = (int) Math.floor(((double) time + Math.sqrt(delta)) / 2);
            var range = tMax - tMin;
            result *= range;
        }
        System.out.println(result);
    }

    public static void part2() {
        long result = 1;
        var lines = AocUtils.readLines("/input/2023/day06.txt");

        var time = Long.valueOf(
                Arrays.stream(lines.get(0).substring("Time:".length()).split(" "))
                        .filter(Predicate.not(String::isBlank))
                        .collect(Collectors.joining(""))
        );

        var distance = Long.parseLong(Arrays.stream(lines.get(1).substring("Distance:".length()).split(" "))
                .filter(Predicate.not(String::isBlank))
                .collect(Collectors.joining("")));

        var delta = time * time - 4 * distance;
        var tMin = (int) Math.floor(((double) time - Math.sqrt(delta)) / 2);
        var tMax = (int) Math.floor(((double) time + Math.sqrt(delta)) / 2);
        var range = tMax - tMin;
        result *= range;
        System.out.println(result);
    }

}

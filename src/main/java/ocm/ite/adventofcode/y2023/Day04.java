package ocm.ite.adventofcode.y2023;

import ocm.ite.adventofcode.AocUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day04 {

    public static void main(String[] args) {
        part1();
        part2();
    }

    public static void part1() {
        int sum = 0;

        for (var line : AocUtils.readLines("/input/2023/day04.txt")) {
            String[] cardData = line.split(":");
            String[] cardId = cardData[0].split(" ");
            var cardNbr = Integer.parseInt(cardId[cardId.length - 1]);

            var data = cardData[1].split("\\|");
            var winning = Arrays.stream(data[0].split(" "))
                    .filter(Predicate.not(String::isEmpty))
                    .map(String::strip)
                    .map(Integer::valueOf)
                    .collect(Collectors.toSet());
            var owned = Arrays.stream(data[1].split(" "))
                    .filter(Predicate.not(String::isEmpty))
                    .map(String::strip)
                    .map(Integer::valueOf)
                    .collect(Collectors.toSet());

            int wins = 0;
            for (Integer o : owned) {
                if (winning.contains(o)) {
                    wins++;
                }
            }
            sum += wins > 0 ? Math.pow(2, wins - 1) : 0;
        }
        System.out.println(sum);
    }


    public static void part2() {
        int sum = 0;
        Map<Integer, Integer> copies = new HashMap<>();

        for (var line : AocUtils.readLines("/input/2023/day04.txt")) {
            String[] cardData = line.split(":");
            String[] cardId = cardData[0].split(" ");
            var cardNbr = Integer.parseInt(cardId[cardId.length - 1]);
            var copiesCount = copies.getOrDefault(cardNbr, 1);
            var data = cardData[1].split("\\|");
            var winning = Arrays.stream(data[0].split(" "))
                    .filter(Predicate.not(String::isEmpty))
                    .map(String::strip)
                    .map(Integer::valueOf)
                    .collect(Collectors.toSet());
            var owned = Arrays.stream(data[1].split(" "))
                    .filter(Predicate.not(String::isEmpty))
                    .map(String::strip)
                    .map(Integer::valueOf)
                    .collect(Collectors.toSet());

            int wins = 0;
            for (Integer o : owned) {
                if (winning.contains(o)) {
                    wins++;
                }
            }
            for (int i = 1; i <= wins; i++) {
                copies.compute(cardNbr + i, (k, ov) -> ov == null ? 1 + copiesCount : ov + copiesCount);
            }
            sum += copiesCount;
        }
        System.out.println(sum);
    }

}

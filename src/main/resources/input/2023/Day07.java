package org.example.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day07 {

    private static final Map<Character, Integer> strengthP1 = Map.of(
            'A', 14,
            'K', 13,
            'Q', 12,
            'J', 1,
            'T', 10,
            '9', 9,
            '8', 8
    );


    private static final Map<Character, Integer> strengthP2 = Map.of(
            '7', 7,
            '6', 6,
            '5', 5,
            '4', 4,
            '3', 3,
            '2', 2
    );

    private static final Map<Character, Integer> strength = new HashMap<>() {{
        putAll(strengthP1);
        putAll(strengthP2);
    }};

    public static void main(String[] args) throws IOException {
        var hands = Files.readAllLines(Path.of("data/input.txt"))
                .stream()
                .map(Hand::of)
                .sorted(Comparator.comparing(Hand::getCategory)
                        .thenComparing(Hand::getC1)
                        .thenComparing(Hand::getC2)
                        .thenComparing(Hand::getC3)
                        .thenComparing(Hand::getC4)
                        .thenComparing(Hand::getC5)
                )
                .collect(Collectors.toList());

        var sum = 0;
        for (int i = 0; i < hands.size(); i++) {
            sum += (i + 1) * hands.get(i).bid;
            System.out.println(hands.get(i).actualHand + " " + (i + 1));
        }
        System.out.println(sum);
    }

    private static class Hand {
        public Integer getCategory() {
            return category;
        }

        private final String actualHand;

        private final Map<Character, Integer> occ;
        private final Integer bid;
        private final Integer category;

        private final int c1;
        private final int c2;
        private final int c3;
        private final int c4;
        private final int c5;

        private Hand(String actualHand, Map<Character, Integer> occ, Integer bid, Integer category) {
            this.actualHand = actualHand;
            this.occ = occ;
            this.bid = bid;
            this.category = category;

            c1 = strength.get(actualHand.charAt(0));
            c2 = strength.get(actualHand.charAt(1));
            c3 = strength.get(actualHand.charAt(2));
            c4 = strength.get(actualHand.charAt(3));
            c5 = strength.get(actualHand.charAt(4));
        }

        static Hand of(String hand) {
            var input = hand.split(" ");
            var handCards = input[0];
            var handBid = input[1];
            var occ = IntStream.range(0, handCards.length()).mapToObj(handCards::charAt)
                    .collect(Collectors.groupingBy(Function.identity()))
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> e.getValue().size()
                    ));
            return new Hand(handCards, occ, Integer.valueOf(handBid), calculateCategory(occ));
        }

        private static Integer calculateCategory(Map<Character, Integer> occ) {
            int occJ = Optional.ofNullable(occ.get('J')).orElse(0);
            var map2 = new HashMap<>(occ);
            map2.remove('J');
            int size = map2.size();
            if (size <= 1) {
                return 7;
            }
            if (size == 2) {
                if (occ.containsValue(4 - occJ)) {
                    return 6;
                }
                return 5;
            }
            if (size == 3) {
                if (occ.containsValue(3 - occJ)) {
                    return 4;
                }
                return 3;
            }
            if (size == 4) {
                return 2;
            }
            return 1;
        }

        public int getC1() {
            return c1;
        }

        public int getC2() {
            return c2;
        }

        public int getC3() {
            return c3;
        }

        public int getC4() {
            return c4;
        }

        public int getC5() {
            return c5;
        }
    }
}

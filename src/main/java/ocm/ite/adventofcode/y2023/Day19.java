package ocm.ite.adventofcode.y2023;

import lombok.SneakyThrows;
import ocm.ite.adventofcode.AocUtils;
import ocm.ite.adventofcode.Range;

import java.util.*;
import java.util.stream.Collectors;

public class Day19 {

    private static final String inputFile = "/input/2023/day19.txt";

    private static final Integer MAX = 4000;
    private static final Integer MIN = 1;

    public static void main(String[] args) {
        var start = System.currentTimeMillis();
        part1();
        System.out.printf("Part 1 duration = %dms%n", (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        part2();
        System.out.printf("Part 2 duration = %dms%n", (System.currentTimeMillis() - start));
    }

    @SneakyThrows
    public static void part1() {
        var workflows = new HashMap<String, Wfw>();
        var partsList = new ArrayList<Parts>();
        var wkfRead = false;
        for (var line : AocUtils.readLines(inputFile)) {
            if (line.isBlank()) {
                wkfRead = true;
                continue;
            }
            if (!wkfRead) {
                var wfw = Wfw.ofLine(line);
                workflows.put(wfw.id, wfw);
            } else {
                partsList.add(Parts.ofLine(line));
            }
        }

        int result = 0;

        for (Parts p : partsList) {
            var currentWfw = workflows.get("in");
            String target = null;
            while (currentWfw != null) {
                for (var condition : currentWfw.conditions) {
                    if (condition.apply(p)) {
                        target = condition.target;
                        currentWfw = workflows.get(target);
                        break;
                    }
                }
            }
            if ("A".equals(target)) {
                result += p.partMap.values().stream().reduce(Integer::sum).orElse(0);
            }
        }
        System.out.println(result);
    }

    @SneakyThrows
    public static void part2() {
        var workflows = new HashMap<String, Wfw>();
        for (var line : AocUtils.readLines(inputFile)) {
            if (line.isBlank()) {
                break;
            }
            var wfw = Wfw.ofLine(line);
            workflows.put(wfw.id, wfw);
        }

        var partsRanges = Map.of(
                'x', Ranges.of(new Range(1, 4000)),
                'm', Ranges.of(new Range(1, 4000)),
                'a', Ranges.of(new Range(1, 4000)),
                's', Ranges.of(new Range(1, 4000))
        );
        var accepted = new ArrayList<Map<Character, Ranges>>();
        exploreAcceptedRanges(partsRanges, workflows.get("in"), workflows, accepted);

        var result = 0L;
        for (var acc : accepted) {
            var product = acc.values()
                    .stream()
                    .map(v -> v.ranges.stream().map(r -> r.max() - r.min() + 1).reduce(Long::sum).orElse(0L))
                    .reduce((l1, l2) -> l1 * l2)
                    .orElse(0L);
            result += product;
        }
        System.out.println(result);
    }

    private static void exploreAcceptedRanges(Map<Character, Ranges> partsRanges, Wfw current, HashMap<String, Wfw> workflows, List<Map<Character, Ranges>> accepted) {
        List<Map.Entry<Map<Character, Ranges>, String>> partRangesToTarget = new ArrayList<>();

        var currentRanges = partsRanges.entrySet()
                .stream()
                .map(e -> Map.entry(e.getKey(), Ranges.copy(e.getValue())))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));

        for (var condition : current.conditions) {
            var conditionRange = condition.asRange();
            if (conditionRange != null) {
                var oldRanges = currentRanges.get(condition.c);
                var newRange = oldRanges.intersect(conditionRange);

                var newPartsRanges = currentRanges.entrySet()
                        .stream()
                        .map(e -> Map.entry(e.getKey(), Ranges.copy(e.getValue())))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue
                        ));
                newPartsRanges.put(condition.c, Ranges.of(newRange));
                partRangesToTarget.add(Map.entry(newPartsRanges, condition.target));

                oldRanges.remove(conditionRange);
            } else {
                partRangesToTarget.add(Map.entry(currentRanges, condition.target));
            }
        }

        for (var e : partRangesToTarget) {
            var target = e.getValue();
            var ranges = e.getKey();
            var next = workflows.get(target);
            if (next != null) {
                exploreAcceptedRanges(ranges, next, workflows, accepted);
            } else if ("A".equals(target)) {
                if (ranges.entrySet().stream().noneMatch(r -> r.getValue().ranges.isEmpty())) {
                    accepted.add(ranges);
                }
            }
        }
    }


    private record Wfw(String id, List<Condition> conditions) {

        private static Wfw ofLine(String line) {
            var id = line.substring(0, line.indexOf('{'));
            var condInput = line.substring(line.indexOf('{') + 1, line.length() - 1);
            var conditions = Arrays.stream(condInput.split(","))
                    .map(Condition::ofInput)
                    .collect(Collectors.toList());
            return new Wfw(id, conditions);
        }
    }

    private record Condition(Character c, Integer bound, int sign, String target) {

        private static Condition ofInput(String input) {
            if (input.contains(":")) {
                var sign = input.contains(">") ? 1 : -1;
                return new Condition(
                        input.charAt(0),
                        Integer.parseInt(input.substring(2, input.indexOf(':'))),
                        sign,
                        input.substring(input.indexOf(':') + 1)
                );
            } else {
                return new Condition(null, null, 0, input);
            }
        }

        private boolean apply(Parts parts) {
            if (c == null) {
                return true;
            } else {
                var value = parts.partMap.get(c);
                return (value - bound) * sign > 0;
            }
        }

        private Range asRange() {
            if (c == null) {
                return null;
            }
            if (sign > 0) {
                return new Range(bound + 1, MAX);
            }
            return new Range(0, bound - 1);
        }
    }

    private record Parts(Map<Character, Integer> partMap) {

        private static Parts ofLine(String line) {
            var partMap = Arrays.stream(line.substring(1, line.length() - 1).split(","))
                    .map(e -> e.split("="))
                    .collect(Collectors.toMap(
                            e -> e[0].charAt(0),
                            e -> Integer.parseInt(e[1])
                    ));
            return new Parts(partMap);
        }
    }

    private record Ranges(List<Range> ranges) {

        private static Ranges of(List<Range> ranges) {
            return new Ranges(new ArrayList<>(ranges));
        }

        private static Ranges of(Range... ranges) {
            return of(Arrays.asList(ranges));
        }

        private static Ranges copy(Ranges r) {
            return of(r.ranges);
        }

        private List<Range> intersect(Range range) {
            var newRanges = new ArrayList<Range>();
            for (Range r : ranges) {
                var intersect = r.intersect(range);
                if (intersect != null) {
                    newRanges.add(intersect);
                }
            }
            return newRanges;
        }

        private void remove(Range range) {
            var newRanges = ranges.stream()
                    .map(r -> r.remove(range))
                    .flatMap(Collection::stream)
                    .toList();
            ranges.clear();
            ranges.addAll(newRanges);
        }
    }

}

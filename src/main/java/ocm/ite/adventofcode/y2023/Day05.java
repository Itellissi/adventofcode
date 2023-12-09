package ocm.ite.adventofcode.y2023;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import ocm.ite.adventofcode.AocUtils;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day05 {

    public static void main(String[] args) {
        part1();
        part2();
    }

    public static void part1() {
        Map<String, MagicMap> magicMaps = new HashMap<>();
        MagicMap currentMap = null;

        List<Long> seedEntries = new ArrayList<>();
        for (var line : AocUtils.readLines("/input/2023/day05.txt")) {
            if (line.startsWith("seeds:")) {
                seedEntries.addAll(
                        Arrays.stream(line.substring(6).split(" "))
                                .map(String::strip)
                                .filter(Predicate.not(String::isEmpty))
                                .map(Long::valueOf)
                                .toList()
                );
            }
            if (currentMap == null) {
                if (line.contains("map:")) {
                    currentMap = new MagicMap();
                    magicMaps.put(line, currentMap);
                }
            } else if (line.isBlank()) {
                currentMap = null;
            } else {
                currentMap.put(line);
            }
        }

        long min = Long.MAX_VALUE;
        for (var entry : seedEntries) {
            var result = entry;
            result = magicMaps.get("seed-to-soil map:").getTargetValue(result);
            result = magicMaps.get("soil-to-fertilizer map:").getTargetValue(result);
            result = magicMaps.get("fertilizer-to-water map:").getTargetValue(result);
            result = magicMaps.get("water-to-light map:").getTargetValue(result);
            result = magicMaps.get("light-to-temperature map:").getTargetValue(result);
            result = magicMaps.get("temperature-to-humidity map:").getTargetValue(result);
            result = magicMaps.get("humidity-to-location map:").getTargetValue(result);

            if (result < min) {
                min = result;
            }
        }
        System.out.println(min);
    }


    public static void part2() {
        Map<String, MagicMap> magicMaps = new HashMap<>();
        MagicMap currentMap = null;

        List<Range> seedRanges = new ArrayList<>();
        for (var line : AocUtils.readLines("/input/2023/day05.txt")) {
            if (line.startsWith("seeds:")) {
                var seedEntries = Arrays.stream(line.substring(6).split(" "))
                        .map(String::strip)
                        .filter(Predicate.not(String::isEmpty))
                        .map(Long::valueOf)
                        .toList();
                for (int i = 0; i < seedEntries.size(); i += 2) {
                    seedRanges.add(new Range(seedEntries.get(i), seedEntries.get(i) + seedEntries.get(i + 1) - 1));
                }
            }
            if (currentMap == null) {
                if (line.contains("map:")) {
                    currentMap = new MagicMap();
                    magicMaps.put(line, currentMap);
                }
            } else if (line.isBlank()) {
                currentMap = null;
            } else {
                currentMap.put(line);
            }
        }

        long min = Long.MAX_VALUE;
        for (var range : seedRanges) {
            var result = List.of(range);
            result = result.stream().map(r -> magicMaps.get("seed-to-soil map:").getTargetRanges(r)).flatMap(Collection::stream).collect(Collectors.toList());
            result = result.stream().map(r -> magicMaps.get("soil-to-fertilizer map:").getTargetRanges(r)).flatMap(Collection::stream).collect(Collectors.toList());
            result = result.stream().map(r -> magicMaps.get("fertilizer-to-water map:").getTargetRanges(r)).flatMap(Collection::stream).collect(Collectors.toList());
            result = result.stream().map(r -> magicMaps.get("water-to-light map:").getTargetRanges(r)).flatMap(Collection::stream).collect(Collectors.toList());
            result = result.stream().map(r -> magicMaps.get("light-to-temperature map:").getTargetRanges(r)).flatMap(Collection::stream).collect(Collectors.toList());
            result = result.stream().map(r -> magicMaps.get("temperature-to-humidity map:").getTargetRanges(r)).flatMap(Collection::stream).collect(Collectors.toList());
            result = result.stream().map(r -> magicMaps.get("humidity-to-location map:").getTargetRanges(r)).flatMap(Collection::stream).collect(Collectors.toList());

            var minimum = result.stream()
                    .min(Comparator.comparing(r -> r.min))
                    .map(r -> r.min)
                    .get();
            if (minimum < min) {
                min = minimum;
            }
        }
        System.out.println(min);
    }


    static class MagicMap {
        TreeSet<MagicMapEntry> entries = new TreeSet<>(Comparator.comparing(MagicMapEntry::getStartSource));

        void put(String line) {
            entries.add(MagicMapEntry.of(line));
        }

        Long getTargetValue(Long val) {
            var res = getTarget(val);
            return res == null ? val : res.map(val);
        }

        MagicMapEntry getTarget(Long val) {
            for (MagicMapEntry e : entries) {
                var result = e.map(val);
                if (result != null) {
                    return e;
                }
            }
            return null;
        }

        List<Range> getTargetRanges(Range range) {
            var result = new ArrayList<Range>();
            long start = range.min;
            long stop = range.max;
            for (MagicMapEntry e : entries) {
                var sourceRange = e.getSourceRange();
                if (sourceRange.min > start) {
                    result.add(new Range(start, Math.min(sourceRange.min - 1, stop)));
                    if (stop < sourceRange.min) {
                        break;
                    } else {
                        if (stop <= sourceRange.max) {
                            result.add(new Range(e.getTargetRange().min, e.map(stop)));
                            break;
                        }
                    }
                    start = sourceRange.max + 1;
                } else if (sourceRange.max >= start) {
                    if (stop <= sourceRange.max) {
                        result.add(new Range(e.map(start), e.map(stop)));
                        break;
                    }
                    result.add(new Range(e.map(start), e.targetRange.max));
                    start = sourceRange.max + 1;
                }
            }
            if (result.isEmpty()) {
                result.add(range);
            }
            return result;
        }
    }

    @Getter
    static class MagicMapEntry {
        Long startSource;
        Long startTarget;

        Range sourceRange;
        Range targetRange;

        Long range;

        static MagicMapEntry of(String line) {
            var result = new MagicMapEntry();
            var list = Arrays.stream(line.split(" "))
                    .map(String::strip)
                    .filter(Predicate.not(String::isEmpty))
                    .map(Long::valueOf)
                    .toList();
            result.startSource = list.get(1);
            result.startTarget = list.get(0);
            result.range = list.get(2);
            result.sourceRange = new Range(result.startSource, result.startSource + result.range - 1);
            result.targetRange = new Range(result.startTarget, result.startTarget + result.range - 1);
            return result;
        }

        boolean accepts(Long value) {
            return value >= startSource && value < startSource + range;
        }

        Long map(Long value) {
            if (accepts(value)) {
                return startTarget + value - startSource;
            } else {
                return null;
            }
        }
    }

    @AllArgsConstructor
    @ToString
    static class Range {
        Long min;
        Long max;
    }
}

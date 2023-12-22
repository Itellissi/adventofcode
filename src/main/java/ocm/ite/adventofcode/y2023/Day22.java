package ocm.ite.adventofcode.y2023;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;
import ocm.ite.adventofcode.AocUtils;
import ocm.ite.adventofcode.Tuple;

import java.util.*;
import java.util.function.Function;

public class Day22 {

    private static final String inputFile = "/input/2023/day22.txt";

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
        var bricks = scanBricks();

        var supports = new HashSet<Brick>();
        for (var brick : bricks) {
            if (brick.supportedBy.size() == 1) {
                supports.add(brick.supportedBy.iterator().next());
            }
        }
        System.out.println(bricks.size() - supports.size());
    }

    @SneakyThrows
    public static void part2() {
        var bricks = scanBricks();

        bricks.sort(Comparator.comparing(b -> b.level));

        int result = 0;
        for (int i = 0; i < bricks.size(); i++) {
            var rootBrick = bricks.get(i);
            var dominoBrick = new HashSet<>(Set.of(rootBrick));
            for (int j = i + 1; j < bricks.size(); j++) {
                var candidate = bricks.get(j);
                if (!candidate.supportedBy.isEmpty()) {
                    var afterSupport = new HashSet<>(candidate.supportedBy);
                    afterSupport.removeAll(dominoBrick);
                    if (afterSupport.size() == 0) {
                        dominoBrick.add(candidate);
                    }
                }
            }
            int dominoBricksSize = dominoBrick.size() - 1;
            if (dominoBricksSize > 0) {
                result += dominoBricksSize;
                //System.out.printf("%s\t%03d\t%d%n", rootBrick, rootBrick.level, dominoBricksSize);
            }
        }
        System.out.println(result);
    }

    private static ArrayList<Brick> scanBricks() {
        var bricks = new ArrayList<Brick>();
        int idx = 0;
//        Function<Integer, String> naming = i -> String.valueOf((char) ('A' + (i)));
        Function<Integer, String> naming = i -> String.format("B%05d", i);
        for (var line : AocUtils.readLines(inputFile)) {
            var coordinates = line.split("~");
            var l = Arrays.stream(coordinates[0].split(",")).map(Integer::valueOf).toArray(Integer[]::new);
            var r = Arrays.stream(coordinates[1].split(",")).map(Integer::valueOf).toArray(Integer[]::new);
            bricks.add(new Brick(
                    naming.apply(idx++),
                    new Tuple<>(Math.min(l[0], r[0]), Math.min(l[1], r[1]), Math.min(l[2], r[2])),
                    new Tuple<>(Math.max(l[0], r[0]), Math.max(l[1], r[1]), Math.max(l[2], r[2]))
            ));
        }
        bricks.sort(Comparator.comparing(b -> b.start.z()));

        var baseBricks = new ArrayList<Brick>();
        for (var current : bricks) {
            int height = 1 + current.end.z() - current.start.z();
            var candidates = new HashSet<Brick>();
            int maxLevel = 0;
            for (int i = baseBricks.size() - 1; i >= 0; i--) {
                var base = baseBricks.get(i);
                if (base.cross(current)) {
                    candidates.add(base);
                    maxLevel = Math.max(maxLevel, base.level);
                }
            }
            int finalMaxLevel = maxLevel;
            candidates.stream()
                    .filter(c -> finalMaxLevel == c.level)
                    .forEach(current.supportedBy::add);
            baseBricks.add(current);
            current.level = height + maxLevel;
        }
        return bricks;
    }

    @RequiredArgsConstructor
    @ToString(of = "id")
    private static class Brick {

        private final String id;
        private final Tuple<Integer> start;
        private final Tuple<Integer> end;
        private int level = -1;

        private final Set<Brick> supportedBy = new HashSet<>();

        private boolean cross(Brick b) {
            boolean xCross = b.end.x() >= start.x() && end.x() >= b.start.x();
            boolean yCross = b.end.y() >= start.y() && end.y() >= b.start.y();
            return xCross && yCross;
        }
    }
}

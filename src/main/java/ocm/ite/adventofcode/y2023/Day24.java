package ocm.ite.adventofcode.y2023;

import lombok.SneakyThrows;
import ocm.ite.adventofcode.AocUtils;
import ocm.ite.adventofcode.Tuple;

import java.util.Arrays;
import java.util.List;

public class Day24 {

    private static final String inputFile = "/input/2023/day24.txt";

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
        var hailStones = readHailStones();
        // var min = 7;
        // var max = 27;
        var min = 200000000000000L;
        var max = 400000000000000L;

        var result = 0;
        for (int i = 0; i < hailStones.size(); i++) {
            var left = hailStones.get(i);

            for (int j = i + 1; j < hailStones.size(); j++) {
                var right = hailStones.get(j);

                var inter = getIntersectionXY(right, left);
                if (inter != null) {
                    if (inter.infinite || (inter.p.x() <= max && inter.p.x() >= min && inter.p.y() <= max && inter.p.y() >= min)) {
                        var leftSign = (inter.p.x() - left.p.x()) * left.v().x();
                        var rightSign = (inter.p.x() - right.p.x()) * right.v().x();
                        if (leftSign >= 0 && rightSign >= 0) {
                            result++;
                        }
                    }
                }
            }

        }
        System.out.println(result);
    }

    @SneakyThrows
    public static void part2() {
        var hailStones = readHailStones();
        // using online solver
    }

    private static List<HailStone> readHailStones() {
        return AocUtils.mapLines(inputFile, l -> {
            var data = l.split("@");
            var pos = Arrays.stream(data[0].split(","))
                    .map(String::strip)
                    .map(Long::parseLong)
                    .toList();
            var v = Arrays.stream(data[1].split(","))
                    .map(String::strip)
                    .map(Long::parseLong)
                    .toList();
            return new HailStone(
                    new Tuple<>(pos.get(0), pos.get(1), pos.get(2)),
                    new Tuple<>(v.get(0), v.get(1), v.get(2))
            );
        });
    }

    private static Intersection getIntersectionXY(HailStone right, HailStone left) {
        // y = a x + b
        // a = Vy / Vx
        var slopeR = (double) right.v.y() / right.v.x();
        var slopeL = (double) left.v.y() / left.v.x();

        var bR = (double) right.p.y() - slopeR * right.p.x();
        var bL = (double) left.p.y() - slopeL * left.p.x();

        if (slopeR == slopeL) {
            return bR == bL ? new Intersection(null, true) : null;
        }
        // a1x + b1 = a2x + b2
        // x = (b2 - b1) / (a2 - a2)
        var xInter = (bR - bL) / (slopeL - slopeR);
        return new Intersection(new Tuple<>(xInter, slopeL * xInter + bL), false);
    }

    private static boolean areCollinear(Tuple<Long> l, Tuple<Long> r) {
        var p = crossProduct(l, r);
        return p.x() == 0 && p.y() == 0 && p.z() == 0;
    }

    private static Tuple<Long> crossProduct(Tuple<Long> l, Tuple<Long> r) {
        var px = l.y() * r.z() - r.y() * l.z();
        var py = l.z() * r.x() - r.z() * l.x();
        var pz = l.x() * r.y() - r.x() * l.y();
        return new Tuple<>(px, py, pz);
    }

    private record HailStone(Tuple<Long> p, Tuple<Long> v) {

    }

    private record Intersection(Tuple<Double> p, boolean infinite) {

    }
}

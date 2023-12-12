package ocm.ite.adventofcode.y2023;

import ocm.ite.adventofcode.AocUtils;
import ocm.ite.adventofcode.Grid;
import ocm.ite.adventofcode.Position;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day11 {

    public static void main(String[] args) {
        part1();
        part2();
    }

    public static void part1() {
        resolve(2);
    }

    public static void part2() {
        resolve(1_000_000);
    }

    private static void resolve(int expansion) {
        long result = 0;
        var grid = Grid.of(
                AocUtils.readLines("/input/2023/day11.txt"),
                s -> s.chars().mapToObj(c -> (char) c).toArray(Character[]::new),
                Character[].class
        );

        List<Position> galaxiesPositions = grid.findAllMatch(c -> c.equals('#'));
        var emptyRows = IntStream.range(0, grid.data().length)
                .boxed()
                .collect(Collectors.toSet());
        var emptyColumns = IntStream.range(0, grid.data()[0].length)
                .boxed()
                .collect(Collectors.toSet());

        galaxiesPositions.forEach(p -> {
            emptyColumns.remove(p.x());
            emptyRows.remove(p.y());
        });
        for (int i = 0; i < galaxiesPositions.size(); i++) {
            for (int j = i + 1; j < galaxiesPositions.size(); j++) {
                long distance = distance(galaxiesPositions.get(i), galaxiesPositions.get(j), emptyRows, emptyColumns, expansion);
                // System.out.printf("distance from %s to %s is %d%n", galaxiesPositions.get(i), galaxiesPositions.get(j), distance);
                result += distance;
            }
        }
        System.out.println(result);
    }

    private static long distance(Position p1, Position p2, Set<Integer> emptyRows, Set<Integer> emptyColumns, long expansion) {
        long xMax = Math.max(p1.x(), p2.x());
        long xMin = Math.min(p1.x(), p2.x());

        long yMax = Math.max(p1.y(), p2.y());
        long yMin = Math.min(p1.y(), p2.y());

        var countEmptyRows = emptyRows.stream()
                .filter(r -> r < yMax && r > yMin)
                .count();

        var countEmptyCols = emptyColumns.stream()
                .filter(c -> c < xMax && c > xMin)
                .count();
        return (xMax - xMin) + (yMax - yMin) + (expansion - 1) * (countEmptyCols + countEmptyRows);
    }
}

package ocm.ite.adventofcode.y2023;

import lombok.SneakyThrows;
import ocm.ite.adventofcode.AocUtils;
import ocm.ite.adventofcode.Grid;
import ocm.ite.adventofcode.Pair;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;

public class Day21 {

    private static final String inputFile = "/input/2023/day21.txt";

    private static final List<Pair> directions = List.of(
            new Pair(0, 1),
            new Pair(1, 0),
            new Pair(0, -1),
            new Pair(-1, 0)
    );

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
        var grid = Grid.of(
                AocUtils.readLines(inputFile),
                s -> s.chars().mapToObj(c -> (char) c).toArray(Character[]::new),
                Character[].class
        );
        var starting = grid.findAnyMatch(c -> c.equals('S'));
        grid.updateElement(starting, '.');

        var result = findPossiblePositions(grid, new PositionData(starting, 64));

        for (var p : result) {
            grid.updateElement(p, 'O');
        }
        grid.updateElement(starting, 'S');
        // grid.print();
        System.out.println(result.size());
    }

    @SneakyThrows
    public static void part2() {
        var grid = Grid.of(
                AocUtils.readLines(inputFile),
                s -> s.chars().mapToObj(c -> (char) c).toArray(Character[]::new),
                Character[].class
        );
        var starting = grid.findAnyMatch(c -> c.equals('S'));
        grid.updateElement(starting, '.');

        // only works with square grid
        var gridSize = grid.data().length;
        var targetSteps = 26501365;

        // 3 points Lagrange
        var x1 = targetSteps % gridSize;
        var x2 = targetSteps % gridSize + gridSize;
        var x3 = targetSteps % gridSize + 2 * gridSize;

        var y1 = findPossiblePositions(grid, new PositionData(starting, (int) x1)).size();
        var y2 = findPossiblePositions(grid, new PositionData(starting, (int) x2)).size();
        var y3 = findPossiblePositions(grid, new PositionData(starting, (int) x3)).size();

        var x = targetSteps;

        var result = BigInteger.valueOf(y1).multiply(BigInteger.valueOf(((long) (x - x2) * (x - x3)) / ((long) (x1 - x2) * (x1 - x3))));
        result = result.add(BigInteger.valueOf(y2).multiply(BigInteger.valueOf(((long) (x - x1) * (x - x3)) / ((long) (x2 - x1) * (x2 - x3)))));
        result = result.add(BigInteger.valueOf(y3).multiply(BigInteger.valueOf(((long) (x - x1) * (x - x2)) / ((long) (x3 - x1) * (x3 - x2)))));

        System.out.println(result);
    }

    private static HashSet<Pair> findPossiblePositions(Grid<Character> grid, PositionData starting) {
        var result = new HashSet<Pair>();
        var visited = new HashSet<Pair>();

        var neighbours = new HashSet<Pair>();
        neighbours.add(starting.p);

        for (int i = 0; i <= starting.r(); i++) {
            var newNeighbours = new HashSet<Pair>();
            for (var neighbour : neighbours) {
                if (i % 2 == starting.r % 2) {
                    result.add(neighbour);
                }
                visited.add(neighbour);
                for (var direction : directions) {
                    var next = new Pair(neighbour.x() + direction.x(), neighbour.y() + direction.y());
                    var nextChar = grid.element(next, true);
                    if (nextChar != null && nextChar == '.') {
                        if (!visited.contains(next)) {
                            newNeighbours.add(next);
                        }
                    }
                }
            }
            neighbours = newNeighbours;
        }
        return result;
    }

    private record PositionData(Pair p, int r) {

    }
}

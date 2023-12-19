package ocm.ite.adventofcode.y2023;

import ocm.ite.adventofcode.AocUtils;
import ocm.ite.adventofcode.Grid;
import ocm.ite.adventofcode.Pair;

import java.util.*;

public class Day17 {

    private static final String inputFile = "/input/2023/day17.txt";

    private static final List<Pair> horizontalDirection = List.of(
            new Pair(1, 0),
            new Pair(-1, 0)
    );

    private static final List<Pair> verticalDirection = List.of(
            new Pair(0, 1),
            new Pair(0, -1)
    );

    public static void main(String[] args) {
        var start = System.currentTimeMillis();
        part1();
        System.out.printf("Part 1 duration = %dms%n", (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        part2();
        System.out.printf("Part 2 duration = %dms%n", (System.currentTimeMillis() - start));
    }

    public static void part1() {
        var grid = Grid.of(
                AocUtils.readLines(inputFile),
                s -> s.chars().mapToObj(c -> ((char) c - '0')).toArray(Integer[]::new),
                Integer[].class
        );

        var min = dijkstra(grid, 0, 3);
        System.out.println(min);
    }

    public static void part2() {
        var grid = Grid.of(
                AocUtils.readLines(inputFile),
                s -> s.chars().mapToObj(c -> ((char) c - '0')).toArray(Integer[]::new),
                Integer[].class
        );
        var min = dijkstra(grid, 4, 10);
        System.out.println(min);
    }

    private static int dijkstra(Grid<Integer> grid, int minSameDir, int maxSameDir) {
        var path1 = new Path(new Pair(1, 0), new Pair(1, 0), 1);
        var path2 = new Path(new Pair(0, 1), new Pair(0, 1), 1);
        var todo = new PriorityQueue<Map.Entry<Path, Integer>>(Map.Entry.comparingByValue());
        var done = new HashSet<Path>();

        todo.add(Map.entry(path1, Objects.requireNonNull(grid.element(1, 0))));
        todo.add(Map.entry(path2, Objects.requireNonNull(grid.element(0, 1))));

        while (!todo.isEmpty()) {
            var e = todo.remove();
            var cost = e.getValue();
            var currentPath = e.getKey();

            var nextDirections = new ArrayList<Pair>();
            if (currentPath.sameDirectionCount >= minSameDir) {
                if (currentPath.d.x() == 0) {
                    nextDirections.addAll(horizontalDirection);
                } else {
                    nextDirections.addAll(verticalDirection);
                }
            }
            if (currentPath.sameDirectionCount < maxSameDir) {
                nextDirections.add(currentPath.d);
            }

            if (done.contains(currentPath)) {
                continue;
            }

            for (var d : nextDirections) {
                var newPos = currentPath.nextPosition(d);
                var newValue = grid.element(newPos);
                if (newValue != null) {
                    if (newPos.x() == grid.data()[0].length - 1 && newPos.y() == grid.data().length - 1) {
                        return newValue + cost;
                    } else {
                        var sameDirectionCount = d == currentPath.d ? currentPath.sameDirectionCount + 1 : 1;
                        var nextPath = new Path(newPos, d, sameDirectionCount);
                        if (!done.contains(nextPath)) {
                            todo.add(Map.entry(nextPath, cost + newValue));
                        }
                    }
                }
            }
            done.add(currentPath);
        }
        return -1;
    }

    private record Path(Pair p, Pair d, int sameDirectionCount) {

        Pair nextPosition(Pair direction) {
            return new Pair(p.x() + direction.x(), p.y() + direction.y());
        }
    }
}

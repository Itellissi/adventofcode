package ocm.ite.adventofcode.y2023;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;
import ocm.ite.adventofcode.AocUtils;
import ocm.ite.adventofcode.Grid;
import ocm.ite.adventofcode.Pair;

import java.util.*;

public class Day23 {

    private static final String inputFile = "/input/2023/day23.txt";

    private static final List<Pair> directions = List.of(
            new Pair(-1, 0),
            new Pair(1, 0),
            new Pair(0, 1),
            new Pair(0, -1)
    );

    private static final Map<Character, Pair> slopes = Map.of(
            'v', new Pair(0, 1),
            '^', new Pair(0, -1),
            '>', new Pair(1, 0),
            '<', new Pair(-1, 0)
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

        var max = new int[grid.data().length][grid.data()[0].length];
        boolean[][] visited = new boolean[grid.data().length][grid.data()[0].length];
        var start = new Pair(1, 0);
        explore(grid, start, 0, max, visited, true);

        System.out.println(max[grid.data().length - 1][grid.data()[0].length - 2]);
    }

    @SneakyThrows
    public static void part2() {
        var grid = Grid.of(
                AocUtils.readLines(inputFile),
                s -> s.chars().mapToObj(c -> (char) c).toArray(Character[]::new),
                Character[].class
        );

        var start = new Pair(1, 0);
        var fin = new Pair(grid.data()[0].length - 2, grid.data().length - 1);

        var network = getIntersectionNetwork(grid, start, fin);
        var maxDistance = findMaxPath(network.get(start), network.get(fin), new boolean[Node.idGen], 0);

        System.out.println(maxDistance);
    }

    private static void explore(Grid<Character> grid, Pair current, int steps, int[][] max, boolean[][] visited, boolean hasSlopes) {
        if (visited[current.y()][current.x()]) {
            return;
        }
        max[current.y()][current.x()] = Math.max(steps, max[current.y()][current.x()]);

        visited[current.y()][current.x()] = true;

        var directions = Day23.directions;
        if (hasSlopes) {
            var forcedDir = slopes.get(grid.element(current));
            directions = forcedDir != null ? List.of(forcedDir) : Day23.directions;
        }
        for (var dir : directions) {
            var next = new Pair(current.x() + dir.x(), current.y() + dir.y());
            var nextChar = grid.element(next);
            if (nextChar != null && !visited[next.y()][next.x()] && nextChar != '#') {
                explore(grid, next, steps + 1, max, visited, hasSlopes);
            }
        }
        visited[current.y()][current.x()] = false;
    }

    private static Map<Pair, Node> getIntersectionNetwork(Grid<Character> grid, Pair start, Pair fin) {
        var q = new LinkedList<QueueData>();
        var network = new HashMap<Pair, Node>();
        var root = new Node(start);
        // cheat code
        q.add(new QueueData(root, new Pair(1, 1)));

        while (!q.isEmpty()) {
            var data = q.removeFirst();
            var from = data.n;
            network.put(from.pos, from);

            var currentPos = data.start;
            var excludeDirection = new Pair(from.pos.x() - currentPos.x(), from.pos.y() - currentPos.y());
            boolean noIntersection;
            Set<Pair> nextItems;
            var distance = 1;
            do {
                final List<Pair> directions;
                var finalExcludeDirection = excludeDirection;
                directions = Day23.directions.stream()
                        .filter(d -> !d.equals(finalExcludeDirection))
                        .toList();
                nextItems = new HashSet<>();
                for (var dir : directions) {
                    var next = new Pair(currentPos.x() + dir.x(), currentPos.y() + dir.y());
                    var nextChar = grid.element(next);
                    if (nextChar != null && nextChar != '#') {
                        nextItems.add(next);
                        excludeDirection = dir.reverse();
                    }
                }
                noIntersection = nextItems.size() == 1;
                if (noIntersection) {
                    currentPos = nextItems.iterator().next();
                    distance++;
                }
            } while (noIntersection);
            // check if it is not a dead end (in case of slopes for instance
            if (!nextItems.isEmpty()) {
                var to = network.get(currentPos);
                if (to == null) {
                    to = new Node(currentPos);
                    // grid.updateElement(currentPos, 'X');
                    network.put(currentPos, to);
                    for (var next : nextItems) {
                        q.add(new QueueData(to, next));
                    }
                }
                from.connections.put(to, distance);
                to.connections.put(from, distance);
            } else if (currentPos.equals(fin)) {
                var to = new Node(currentPos);
                network.put(currentPos, to);
                from.connections.put(to, distance);
                to.connections.put(from, distance);
            }
        }
        // grid.print();
        return network;
    }

    private static int findMaxPath(Node current, Node fin, boolean[] visited, int cumul) {
        if (current == fin) {
            return cumul;
        }
        visited[current.id] = true;
        int maxCost = 0;
        for (var e : current.connections.entrySet()) {
            if (!visited[e.getKey().id]) {
                maxCost = Math.max(maxCost, findMaxPath(e.getKey(), fin, visited, cumul + e.getValue()));
            }
        }
        visited[current.id] = false;
        return maxCost;
    }

    @RequiredArgsConstructor
    @ToString(of = {"id", "pos"})
    private static class Node {

        private static int idGen = 0;

        private final int id = idGen++;

        private final Pair pos;

        private final Map<Node, Integer> connections = new HashMap<>();
    }

    private record QueueData(Node n, Pair start) {

    }
}

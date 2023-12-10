package ocm.ite.adventofcode.y2023;

import ocm.ite.adventofcode.AocUtils;
import ocm.ite.adventofcode.Grid;
import ocm.ite.adventofcode.Position;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day10 {

    private static final List<Position> directions = List.of(
            new Position(-1, 0),
            new Position(1, 0),
            new Position(0, 1),
            new Position(0, -1)
    );

    private static final Map<Position, Set<Character>> directionCompatibilityMap = Map.of(
            directions.get(2), Set.of('|', '7', 'F', 'S'),
            directions.get(3), Set.of('|', 'L', 'J', 'S'),
            directions.get(1), Set.of('-', 'L', 'F', 'S'),
            directions.get(0), Set.of('-', '7', 'J', 'S')
    );

    public static void main(String[] args) {
        part1();
        part2();
    }

    public static void part1() {
        var grid = Grid.of(
                AocUtils.readLines("/input/2023/day10.txt"),
                l -> l.chars().mapToObj(c -> (char) c).toArray(Character[]::new),
                Character[].class
        );

        var start = grid.findAnyMatch(c -> c.equals('S'));

        var loop = explore(grid, start);
        System.out.println((loop.size() + 1) / 2);
    }


    public static void part2() {
        var grid = Grid.of(
                AocUtils.readLines("/input/2023/day10.txt"),
                l -> l.chars().mapToObj(c -> (char) c).toArray(Character[]::new),
                Character[].class
        );

        var start = grid.findAnyMatch(c -> c.equals('S'));

        var loop = explore(grid, start);
        var boundaries = new HashSet<>(loop);


        for (int y = 0; y < grid.data().length; y++) {
            for (int x = 0; x < grid.data()[y].length; x++) {
                if (!boundaries.contains(new Position(x, y))) {
                    // for debug
                    grid.updateElement(x, y, 'x');
                } else {
                    switch (Objects.requireNonNull(grid.element(x, y))) {
                        // │┌┐└┘─
                        // |F7LJ-
                        case '|' -> grid.updateElement(x, y, '│');
                        case 'F' -> grid.updateElement(x, y, '┌');
                        case '7' -> grid.updateElement(x, y, '┐');
                        case 'L' -> grid.updateElement(x, y, '└');
                        case 'J' -> grid.updateElement(x, y, '┘');
                        case '-' -> grid.updateElement(x, y, '─');
                        // cheat code
                        case 'S' -> grid.updateElement(x, y, '┌');
                    }
                }
            }
        }

        boolean isOpen;
        int count = 0;
        var closing = Set.of('┐', '┘');
        var openToClose = Map.of(
                '┌', '┘',
                '└', '┐'
        );

        for (var chars : grid.data()) {
            isOpen = false;
            for (int x = 0; x < chars.length; x++) {
                char currChar = chars[x];
                if (currChar == 'x') {
                    if (isOpen) {
                        chars[x] = 'I';
                        count++;
                    } else {
                        chars[x] = 'O';
                    }
                } else {
                    switch (currChar) {
                        case '│' -> isOpen = !isOpen;
                        case '┌', '└' -> {
                            var expected = openToClose.get(currChar);
                            x++;
                            var nextChar = chars[x];
                            while (!closing.contains(nextChar)) {
                                x++;
                                nextChar = chars[x];
                            }
                            if (nextChar.equals(expected)) {
                                isOpen = !isOpen;
                            }
                        }
                    }
                }
            }
        }
        grid.print();
        System.out.println(count);
    }

    private static List<Position> explore(Grid<Character> grid, Position start) {
        // cheat code 2
        Position[] nodes = new Position[100000];
        LinkedList<ExploreInput> stack = new LinkedList<>();
        stack.addLast(new ExploreInput(start, 0, null));
        while (!stack.isEmpty()) {
            var curr = stack.removeLast();
            nodes[curr.i] = curr.pos;
            for (Position direction : directions) {
                if (direction.equals(curr.exceptDirection)) {
                    continue;
                }
                var next = connect(curr.pos, grid, direction);
                if (next != null) {
                    var candidate = Objects.requireNonNull(grid.element(next));
                    if (candidate == 'S') {
                        return IntStream.range(0, curr.i + 1).boxed().map(idx -> nodes[idx])
                                .collect(Collectors.toList());
                    } else {
                        stack.addLast(new ExploreInput(next, curr.i + 1, new Position(-direction.x(), -direction.y())));
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    private static Position connect(Position pos, Grid<Character> grid, Position direction) {
        var curr = Objects.requireNonNull(grid.element(pos));
        if (curr == 'S' || directionCompatibilityMap.get(direction).contains(curr)) {
            var nextPos = new Position(pos.x() + direction.x(), pos.y() + direction.y());
            Position revertDirection = new Position(-direction.x(), -direction.y());
            return grid.isInBounds(nextPos) && directionCompatibilityMap.get(revertDirection).contains(grid.element(nextPos)) ?
                    nextPos :
                    null;
        }
        return null;
    }

    private record ExploreInput(Position pos, int i, Position exceptDirection) {

    }
}

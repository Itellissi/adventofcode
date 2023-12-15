package ocm.ite.adventofcode.y2023;

import ocm.ite.adventofcode.AocUtils;
import ocm.ite.adventofcode.Grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day14 {

    private static final String inputFile = "/input/2023/day14.txt";

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
                s -> s.chars().mapToObj(c -> (char) c).toArray(Character[]::new),
                Character[].class
        );

        tiltGridToTheNorth(grid);
        grid.print();

        System.out.println(calculateWeight(grid));
    }

    public static void part2() {
        int target = 1000000000;
        var grid = Grid.of(
                AocUtils.readLines(inputFile),
                s -> s.chars().mapToObj(c -> (char) c).toArray(Character[]::new),
                Character[].class
        );

        List<String> signs = new ArrayList<>();
        List<Long> weights = new ArrayList<>();

        int cycleSize = -1, offset = -1;
        while (offset < 0 || weights.size() < cycleSize) {
            tiltGridToTheNorth(grid);
            tiltGridToTheWest(grid);
            tiltGridToTheSouth(grid);
            tiltGridToTheEast(grid);
            if (offset < 0) {
                var sig = calculateGridSignature(grid);
                if (signs.contains(sig)) {
                    offset = signs.indexOf(sig);
                    cycleSize = signs.size() - offset;
                }
                signs.add(sig);
            }

            if (offset > 0) {
                weights.add(calculateWeight(grid));
            }
        }

        int idx = (target - 1 - offset) % cycleSize;

        System.out.println(weights.get(idx));
    }

    private static String calculateGridSignature(Grid<Character> grid) {
        var result = new StringBuilder();
        Arrays.stream(grid.data())
                .flatMap(Arrays::stream)
                .forEach(result::append);
        return result.toString();
    }

    private static void tiltGridToTheNorth(Grid<Character> grid) {
        for (int c = 0; c < grid.data()[0].length; c++) {
            int prev = 0;
            for (int r = 0; r < grid.data().length; r++) {
                switch (grid.data()[r][c]) {
                    case '#' -> prev = r + 1;
                    case 'O' -> {
                        if (prev < r) {
                            grid.data()[prev][c] = 'O';
                            grid.data()[r][c] = '.';
                        }
                        prev++;
                    }
                }
            }
        }
    }

    private static void tiltGridToTheSouth(Grid<Character> grid) {
        for (int c = 0; c < grid.data()[0].length; c++) {
            int prev = grid.data().length - 1;
            for (int r = grid.data().length - 1; r >= 0; r--) {
                switch (grid.data()[r][c]) {
                    case '#' -> prev = r - 1;
                    case 'O' -> {
                        if (prev > r) {
                            grid.data()[prev][c] = 'O';
                            grid.data()[r][c] = '.';
                        }
                        prev--;
                    }
                }
            }
        }
    }

    private static void tiltGridToTheWest(Grid<Character> grid) {
        for (int r = 0; r < grid.data().length; r++) {
            int prev = 0;
            for (int c = 0; c < grid.data()[0].length; c++) {
                switch (grid.data()[r][c]) {
                    case '#' -> prev = c + 1;
                    case 'O' -> {
                        if (prev < c) {
                            grid.data()[r][prev] = 'O';
                            grid.data()[r][c] = '.';
                        }
                        prev++;
                    }
                }
            }
        }
    }

    private static void tiltGridToTheEast(Grid<Character> grid) {
        for (int r = 0; r < grid.data().length; r++) {
            int prev = grid.data()[0].length - 1;
            for (int c = grid.data()[0].length - 1; c >= 0; c--) {
                switch (grid.data()[r][c]) {
                    case '#' -> prev = c - 1;
                    case 'O' -> {
                        if (prev > c) {
                            grid.data()[r][prev] = 'O';
                            grid.data()[r][c] = '.';
                        }
                        prev--;
                    }
                }
            }
        }
    }

    private static long calculateWeight(Grid<Character> grid) {
        var result = 0;
        int w = grid.data().length;
        for (int i = 0; i < grid.data().length; i++) {
            for (int j = 0; j < grid.data()[i].length; j++) {
                if (grid.data()[i][j] == 'O') {
                    result += w;
                }
            }
            w--;
        }
        return result;
    }

}

package ocm.ite.adventofcode.y2023;

import ocm.ite.adventofcode.AocUtils;
import ocm.ite.adventofcode.Grid;
import ocm.ite.adventofcode.Position;

import java.util.HashSet;

public class Day16 {

    private static final String inputFile = "/input/2023/day16.txt";

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

        var startBeam = new Beam(new Position(0, 0), new Position(1, 0));
        long countEnergized = countEnergized(grid, startBeam);
        System.out.println(countEnergized);
    }

    public static void part2() {
        var grid = Grid.of(
                AocUtils.readLines(inputFile),
                s -> s.chars().mapToObj(c -> (char) c).toArray(Character[]::new),
                Character[].class
        );

        var max = 0L;
        for (int y = 0; y < grid.data().length; y++) {
            var startBeam = new Beam(new Position(0, y), new Position(1, 0));
            long countEnergized = countEnergized(grid, startBeam);
            max = Math.max(max, countEnergized);

            startBeam = new Beam(new Position(grid.data()[y].length - 1, y), new Position(-1, 0));
            countEnergized = countEnergized(grid, startBeam);
            max = Math.max(max, countEnergized);
        }
        for (int x = 0; x < grid.data().length; x++) {
            var startBeam = new Beam(new Position(x, 0), new Position(0, 1));
            long countEnergized = countEnergized(grid, startBeam);
            max = Math.max(max, countEnergized);

            startBeam = new Beam(new Position(x, grid.data().length - 1), new Position(0, -1));
            countEnergized = countEnergized(grid, startBeam);
            max = Math.max(max, countEnergized);
        }

        System.out.println(max);
    }

    private static long countEnergized(Grid<Character> grid, Beam startBeam) {
        var existingBeams = new HashSet<Beam>();
        navigate(startBeam, grid, existingBeams);
        return existingBeams.stream()
                .map(Beam::p)
                .distinct()
                .count();
    }

    private static void navigate(Beam currentBeam, Grid<Character> grid, HashSet<Beam> existingBeams) {
        if (existingBeams.contains(currentBeam)) {
            return;
        }
        var c = grid.element(currentBeam.p());
        if (c == null) {
            return;
        }
        existingBeams.add(currentBeam);
        switch (c) {
            case '.' -> {
                var nextBeam = new Beam(currentBeam.nextPosition(currentBeam.direction), currentBeam.direction());
                navigate(nextBeam, grid, existingBeams);
            }
            case '/' -> {
                var newDirection = new Position(-currentBeam.direction.y(), -currentBeam.direction.x());
                var nextBeam = new Beam(currentBeam.nextPosition(newDirection), newDirection);
                navigate(nextBeam, grid, existingBeams);
            }
            case '\\' -> {
                var newDirection = new Position(currentBeam.direction.y(), currentBeam.direction.x());
                var nextBeam = new Beam(currentBeam.nextPosition(newDirection), newDirection);
                navigate(nextBeam, grid, existingBeams);
            }
            case '|' -> {
                // split
                if (currentBeam.direction.x() != 0) {
                    var direction1 = new Position(0, 1);
                    var direction2 = new Position(0, -1);
                    var nextBeam1 = new Beam(currentBeam.nextPosition(direction1), direction1);
                    var nextBeam2 = new Beam(currentBeam.nextPosition(direction2), direction2);
                    navigate(nextBeam1, grid, existingBeams);
                    navigate(nextBeam2, grid, existingBeams);
                } else {
                    var nextBeam = new Beam(currentBeam.nextPosition(currentBeam.direction), currentBeam.direction());
                    navigate(nextBeam, grid, existingBeams);
                }
            }
            case '-' -> {
                // split
                if (currentBeam.direction.y() != 0) {
                    var direction1 = new Position(1, 0);
                    var direction2 = new Position(-1, 0);
                    var nextBeam1 = new Beam(currentBeam.nextPosition(direction1), direction1);
                    var nextBeam2 = new Beam(currentBeam.nextPosition(direction2), direction2);
                    navigate(nextBeam1, grid, existingBeams);
                    navigate(nextBeam2, grid, existingBeams);
                } else {
                    var nextBeam = new Beam(currentBeam.nextPosition(currentBeam.direction), currentBeam.direction());
                    navigate(nextBeam, grid, existingBeams);
                }
            }
            default -> throw new RuntimeException();
        }
    }

    private record Beam(Position p, Position direction) {

        Position nextPosition(Position direction) {
            return new Position(p.x() + direction.x(), p.y() + direction.y());
        }
    }
}

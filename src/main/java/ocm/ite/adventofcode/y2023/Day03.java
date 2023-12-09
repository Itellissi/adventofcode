package ocm.ite.adventofcode.y2023;

import ocm.ite.adventofcode.AocUtils;

import java.util.ArrayList;
import java.util.List;

public class Day03 {

    private static final int[][] directions = new int[][]{
            {0, 1},
            {0, -1},
            {1, 0},
            {-1, 0},
            {1, 1},
            {1, -1},
            {-1, 1},
            {-1, -1}
    };

    public static void main(String[] args) {
        part1();
        part2();
    }

    public static void part1() {
        var matrix = AocUtils.mapLines("/input/2023/day03.txt", String::toCharArray);

        int sum = 0;
        boolean[][] visited = new boolean[matrix.size()][matrix.get(0).length];
        for (int i = 0; i < matrix.size(); i++) {
            char[] row = matrix.get(i);
            for (int j = 0; j < row.length; j++) {
                char currentChar = row[j];
                if (currentChar != '.' && !Character.isDigit(currentChar)) {
                    sum += extractNeighboursSum(i, j, matrix, visited);
                }
            }
        }
        System.out.println(sum);
    }


    public static void part2() {
        var matrix = AocUtils.mapLines("/input/2023/day03.txt", String::toCharArray);

        int sum = 0;
        for (int i = 0; i < matrix.size(); i++) {
            char[] row = matrix.get(i);
            for (int j = 0; j < row.length; j++) {
                char currentChar = row[j];
                if (currentChar == '*') {
                    sum += exploreNeighbours(i, j, matrix);
                }
            }
        }
        System.out.println(sum);
    }

    private static Integer extractNeighboursSum(int i, int j, List<char[]> matrix, boolean[][] visited) {
        int rmax = matrix.size();
        int cmax = matrix.get(0).length;

        int r, c;
        int sum = 0;
        for (var direction : directions) {
            r = i + direction[0];
            c = j + direction[1];
            if (isInRange(rmax, cmax, r, c) && !visited[r][c] && Character.isDigit(matrix.get(r)[c])) {
                sum += extractNumber(matrix.get(r), c, visited[r]);
            }
        }
        return sum;
    }

    private static Integer exploreNeighbours(int i, int j, List<char[]> matrix) {
        int rmax = matrix.size();
        int cmax = matrix.get(0).length;

        int r, c;
        boolean[][] visited = new boolean[3][cmax];
        var numbers = new ArrayList<Integer>();
        for (var direction : directions) {
            r = i + direction[0];
            c = j + direction[1];
            if (isInRange(rmax, cmax, r, c) && !visited[direction[0] + 1][c] && Character.isDigit(matrix.get(r)[c])) {
                if (numbers.size() == 2) {
                    return 0;
                }
                numbers.add(extractNumber(matrix.get(r), c, visited[direction[0] + 1]));
            }
        }
        return numbers.size() == 2 ? numbers.get(0) * numbers.get(1) : 0;
    }

    private static int extractNumber(char[] row, int c, boolean[] visitedRow) {
        int j = c;
        int value = 0;
        while (j + 1 < row.length && Character.isDigit(row[j + 1])) {
            j++;
        }
        int multiplier = 1;
        while (j >= 0 && Character.isDigit(row[j])) {
            visitedRow[j] = true;
            value += multiplier * (row[j] - '0');
            multiplier *= 10;
            j--;
        }
        return value;
    }

    private static boolean isInRange(int rmax, int cmax, int r, int c) {
        return r >= 0 && r < rmax && c >= 0 && c < cmax;
    }
}

package ocm.ite.adventofcode.y2023;

import ocm.ite.adventofcode.AocUtils;

import java.util.ArrayList;
import java.util.List;

public class Day13 {

    public static void main(String[] args) {
        part1();
        part2();
    }

    public static void part1() {
        long result = 0;
        var rows = new ArrayList<String>();
        var cols = new ArrayList<String>();
        for (var line : AocUtils.readLines("/input/2023/day13.txt")) {
            if (line.isEmpty()) {
                int[] mirrors = extractMirrors(rows, cols);
                System.out.println(mirrors[0] + "  " + mirrors[1]);
                rows = new ArrayList<>();
                cols = new ArrayList<>();
                result += mirrors[0] + mirrors[1] * 100;
            } else {
                rows.add(line);
                int i = 0;
                for (Character c : line.toCharArray()) {
                    if (i < cols.size()) {
                        cols.set(i, cols.get(i) + c);
                    } else {
                        cols.add(String.valueOf(c));
                    }
                    i++;
                }
            }
        }
        int[] mirrors = extractMirrors(rows, cols);
        System.out.println(mirrors[0] + "  " + mirrors[1]);

        result += mirrors[0] + mirrors[1] * 100;

        System.out.println(result);
    }

    public static void part2() {
        long result = 0;
        var rows = new ArrayList<String>();
        var cols = new ArrayList<String>();
        for (var line : AocUtils.readLines("/input/2023/day13.txt")) {
            if (line.isEmpty()) {
                int[] mirrors = extractMirrorsPart2(rows, cols);
                System.out.println(mirrors[0] + "  " + mirrors[1]);
                rows = new ArrayList<>();
                cols = new ArrayList<>();
                result += mirrors[0] + mirrors[1] * 100;
            } else {
                rows.add(line);
                int i = 0;
                for (Character c : line.toCharArray()) {
                    if (i < cols.size()) {
                        cols.set(i, cols.get(i) + c);
                    } else {
                        cols.add(String.valueOf(c));
                    }
                    i++;
                }
            }
        }
        int[] mirrors = extractMirrorsPart2(rows, cols);
        System.out.println(mirrors[0] + "  " + mirrors[1]);

        result += mirrors[0] + mirrors[1] * 100;

        System.out.println(result);
    }

    private static int[] extractMirrors(List<String> rows, List<String> cols) {
        int colMirror = 0;
        int rowMirror = 0;

        colMirror = extractMirror(cols);
        if (colMirror == 0) {
            rowMirror = extractMirror(rows);
        }

        return new int[]{colMirror, rowMirror};
    }

    private static int extractMirror(List<String> cols) {
        for (int i = 1; i < cols.size(); i++) {
            if (cols.get(i).equals(cols.get(i - 1))) {
                if (checkIsRealMirror(cols, i)) {
                    return i;
                }
            }
        }
        return 0;
    }

    private static boolean checkIsRealMirror(List<String> cols, int i) {
        for (int j = i + 1; j < cols.size() && 2 * i - 1 - j >= 0; j++) {
            if (!cols.get(j).equals(cols.get(2 * i - 1 - j))) {
                return false;
            }
        }
        return true;
    }

    private static int[] extractMirrorsPart2(List<String> rows, List<String> cols) {
        return new int[]{fixSmudge(cols), fixSmudge(rows)};
    }

    private static int fixSmudge(List<String> rows) {
        for (int i = 1; i < rows.size(); i++) {
            String left = rows.get(i);
            String right = rows.get(i - 1);
            int diffs = 0;
            if (!right.equals(left)) {
                for (int k = 0; k < right.length(); k++) {
                    if (right.charAt(k) != left.charAt(k)) {
                        diffs++;
                        if (diffs > 1) {
                            break;
                        }
                    }
                }
            }
            if (diffs <= 1) {
                if (checkIsRealMirrorPart2(rows, i, diffs)) {
                    return i;
                }
            }
        }
        return 0;
    }

    private static boolean checkIsRealMirrorPart2(List<String> cols, int i, int diffs) {
        for (int j = i + 1; j < cols.size() && 2 * i - 1 - j >= 0; j++) {
            var right = cols.get(j);
            var left = cols.get(2 * i - 1 - j);
            if (!right.equals(left)) {
                for (int k = 0; k < right.length(); k++) {
                    if (right.charAt(k) != left.charAt(k)) {
                        diffs++;
                        if (diffs > 1) {
                            return false;
                        }
                    }
                }
            }
        }
        return diffs == 1;
    }

}

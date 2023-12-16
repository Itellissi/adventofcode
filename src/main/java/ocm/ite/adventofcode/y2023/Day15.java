package ocm.ite.adventofcode.y2023;

import ocm.ite.adventofcode.AocUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Day15 {

    private static final String inputFile = "/input/2023/day15.txt";

    public static void main(String[] args) {
        var start = System.currentTimeMillis();
        part1();
        System.out.printf("Part 1 duration = %dms%n", (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        part2();
        System.out.printf("Part 2 duration = %dms%n", (System.currentTimeMillis() - start));
    }

    public static void part1() {
        var entries = AocUtils.readLines(inputFile).get(0).split(",");
        long result = 0;
        for (String e : entries) {
            int hash = calculateHash(e);
            // System.out.println(hash);
            result += hash;
        }
        System.out.println(result);
    }

    public static void part2() {
        var entries = AocUtils.readLines(inputFile).get(0).split(",");
        long result = 0;
        Map<Integer, LinkedList<String[]>> boxMap = new HashMap<>();
        for (String e : entries) {

            boolean isMinus = e.endsWith("-");
            var label = isMinus ? e.substring(0, e.length() - 1) : e.substring(0, e.length() - 2);
            var focal = isMinus ? 0 : e.charAt(e.length() - 1);

            int hash = calculateHash(label);
            var list = boxMap.computeIfAbsent(hash, h -> new LinkedList<>());

            if (isMinus) {
                int i = 0;
                boolean found = false;
                while (!found && i < list.size()) {
                    if (list.get(i)[0].equals(label)) {
                        found = true;
                        list.remove(i);
                    }
                    i++;
                }
            } else {
                boolean found = false;
                int i = 0;
                while (!found && i < list.size()) {
                    if (list.get(i)[0].equals(label)) {
                        found = true;
                        list.set(i, new String[]{label, String.valueOf(focal)});
                    }
                    i++;
                }
                if (!found) {
                    list.addLast(new String[]{label, String.valueOf(focal)});
                }
            }
        }

        for (Map.Entry<Integer, LinkedList<String[]>> e : boxMap.entrySet()) {
            for (int i = 0; i < e.getValue().size(); i++) {
                result += (long) (i + 1) * (e.getKey() + 1) * Integer.parseInt(e.getValue().get(i)[1]);
            }
        }
        System.out.println(result);
    }

    private static int calculateHash(String e) {
        var hash = 0;
        for (char c : e.toCharArray()) {
            hash += c;
            hash *= 17;
            hash = hash % 256;
        }
        return hash;
    }

}

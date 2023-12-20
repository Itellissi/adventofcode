package ocm.ite.adventofcode.y2023;

import ocm.ite.adventofcode.AocUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static ocm.ite.adventofcode.AocUtils.ppcm;

public class Day08 {

    public static void main(String[] args) {
        part1();
        part2();
    }

    public static void part1() {
        var pattern = Pattern.compile("(\\w{3}) = \\((\\w{3}), (\\w{3})\\)");
        Map<String, Node> network = new HashMap<>();
        var linesIterator = AocUtils.readLines("/input/2023/day08.txt").iterator();

        var instructions = linesIterator.next();
        linesIterator.next();
        String line;
        while (linesIterator.hasNext()) {
            line = linesIterator.next();
            var matcher = pattern.matcher(line);
            if (matcher.find()) {
                var currentNode = matcher.group(1);
                var leftNode = matcher.group(2);
                var rightNode = matcher.group(3);

                var curr = network.computeIfAbsent(currentNode, Node::new);
                curr.left = network.computeIfAbsent(leftNode, Node::new);
                curr.right = network.computeIfAbsent(rightNode, Node::new);
            }
        }
        long i = 0;
        Node curr = network.get("AAA");

        while (!curr.label.equals("ZZZ")) {
            switch (instructions.charAt((int) (i % instructions.length()))) {
                case 'L' -> curr = curr.left;
                case 'R' -> curr = curr.right;
            }
            i++;
        }

        System.out.println(i);
    }

    public static void part2() {
        var pattern = Pattern.compile("(\\w{3}) = \\((\\w{3}), (\\w{3})\\)");
        Map<String, Node> network = new HashMap<>();
        List<Node> starting = new ArrayList<>();
        var linesIterator = AocUtils.readLines("/input/2023/day08.txt").iterator();

        var instructions = linesIterator.next();
        linesIterator.next();
        String line;
        while (linesIterator.hasNext()) {
            line = linesIterator.next();
            var matcher = pattern.matcher(line);
            if (matcher.find()) {
                var currentNode = matcher.group(1);
                var leftNode = matcher.group(2);
                var rightNode = matcher.group(3);

                var curr = network.computeIfAbsent(currentNode, Node::new);
                curr.left = network.computeIfAbsent(leftNode, Node::new);
                curr.right = network.computeIfAbsent(rightNode, Node::new);
                if (curr.label.endsWith("A")) {
                    starting.add(curr);
                }
                if (curr.label.endsWith("Z")) {
                    curr.isFinal = true;
                }
            }
        }
        long i = 0;
        Node[] curr = starting.toArray(new Node[starting.size()]);
        long[] prevFinal = new long[starting.size()];
        long[] cycleStart = new long[starting.size()];
        long[] prevCycle = new long[starting.size()];
        boolean[] hasCycle = new boolean[starting.size()];

        boolean endReached = false;
        boolean allHaveCycle = false;
        while (!endReached && !allHaveCycle) {
            endReached = true;
            allHaveCycle = false;
            switch (instructions.charAt((int) (i % instructions.length()))) {
                case 'L' -> {
                    for (int j = 0; j < curr.length; j++) {
                        curr[j] = curr[j].left;
                        endReached = endReached && curr[j].isFinal;
                        if (curr[j].isFinal) {
                            if (cycleStart[j] == 0) {
                                cycleStart[j] = i;
                            }
                            if (prevFinal[j] != 0) {
                                var cycle = i - prevFinal[j];
                                if (prevCycle[j] == cycle) {
                                    hasCycle[j] = true;
                                    allHaveCycle = true;
                                    for (boolean hc : hasCycle) {
                                        allHaveCycle = allHaveCycle && hc;
                                    }
                                }
                                prevCycle[j] = cycle;
                            }
                            prevFinal[j] = i;
                        }
                    }
                }
                case 'R' -> {
                    for (int j = 0; j < curr.length; j++) {
                        curr[j] = curr[j].right;
                        endReached = endReached && curr[j].isFinal;
                        if (curr[j].isFinal) {
                            if (cycleStart[j] == 0) {
                                cycleStart[j] = i;
                            }
                            if (prevFinal[j] != 0) {
                                var cycle = i - prevFinal[j];
                                if (prevCycle[j] == cycle) {
                                    hasCycle[j] = true;
                                    allHaveCycle = true;
                                    for (boolean hc : hasCycle) {
                                        allHaveCycle = allHaveCycle && hc;
                                    }
                                }
                                prevCycle[j] = cycle;
                            }
                            prevFinal[j] = i;
                        }
                    }
                }
            }
            i++;
        }
        var rs = 1L;
        for (long c : prevCycle) {
            rs = ppcm(rs, c);
        }

        System.out.println(rs);
    }

    private static class Node {
        private final String label;

        boolean isFinal;

        private Node left;
        private Node right;

        private Node(String label) {
            this.label = label;
        }
    }
}

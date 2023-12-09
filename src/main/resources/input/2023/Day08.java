package org.example.aoc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Day08 {

    public static void main(String[] args) throws IOException {
        long result = 1;
        var pattern = Pattern.compile("(\\w{3}) = \\((\\w{3}), (\\w{3})\\)");
        Map<String, Node> network = new HashMap<>();
        List<Node> starting = new ArrayList<>();
        try (var r = new BufferedReader(new FileReader("data/input.txt"))) {
            var instructions = r.readLine();
            r.readLine();
            String line;
            while ((line = r.readLine()) != null) {
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
            System.out.println(network.values().stream().filter(n -> n.isFinal).count());
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
                    case 'L':
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
                        break;
                    case 'R':
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
                        break;
                }
                i++;
            }
            var rs = 1L;
            for(long c : prevCycle) {
                rs = ppcm(rs, c);
            }
            System.out.println(rs);
        }
        System.out.println(result);
    }

    public static long ppcm (long Nb1, long Nb2) {
        long Produit, Reste, PPCM;

        Produit = Nb1*Nb2;
        Reste   = Nb1%Nb2;
        while(Reste != 0){
            Nb1 = Nb2;
            Nb2 = Reste;
            Reste = Nb1%Nb2;
        }
        PPCM = Produit/Nb2;
        //		System.out.println("PGCD = " + Nb2 + " PPCM = " + PPCM);
        return PPCM;
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

package ocm.ite.adventofcode.y2023;

import lombok.SneakyThrows;
import ocm.ite.adventofcode.AocUtils;
import org.jgrapht.alg.StoerWagnerMinimumCut;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.Arrays;
import java.util.function.Predicate;

public class Day25 {

    private static final String inputFile = "/input/2023/day25.txt";

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
        var graph = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        for (var l : AocUtils.readLines(inputFile)) {
            var desc = l.split(":");
            var compName = desc[0];
            var links = Arrays.stream(desc[1].split(" "))
                    .map(String::strip)
                    .filter(Predicate.not(String::isBlank))
                    .toList();

            graph.addVertex(compName);
            for (var link : links) {
                graph.addVertex(link);
                graph.addEdge(link, compName);
            }
        }
        var totalV = graph.vertexSet().size();

        var cut = new StoerWagnerMinimumCut<>(graph);
        double cutWeight = cut.minCutWeight();
        // expected 3
        System.out.println(cutWeight);
        var subSet = cut.minCut();
        int leftSize = subSet.size();
        int rightSize = totalV - leftSize;
        System.out.printf("subset 1 = %d, subset 2 = %d, expected result = %d%n", leftSize, rightSize, (leftSize * rightSize));
    }

    @SneakyThrows
    public static void part2() {
        System.out.println("I pressed the button");
    }
}

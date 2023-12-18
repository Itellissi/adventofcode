package ocm.ite.adventofcode.y2023;

import lombok.SneakyThrows;
import ocm.ite.adventofcode.AocUtils;
import ocm.ite.adventofcode.Position;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Day18 {

    private static final String inputFile = "/input/2023/day18.txt";

    private static final Position RIGHT = new Position(1, 0);
    private static final Position LEFT = new Position(-1, 0);
    private static final Position UP = new Position(0, -1);
    private static final Position DOWN = new Position(0, 1);

    private static final Map<String, Position> directionMapper = Map.of(
            "R", RIGHT,
            "L", LEFT,
            "U", UP,
            "D", DOWN
    );

    private static final Map<Character, Position> directionMapper2 = Map.of(
            '0', RIGHT,
            '2', LEFT,
            '3', UP,
            '1', DOWN
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
        var instructions = AocUtils.mapLines(inputFile, Instruction::ofLine);
        var p = createPolygon(instructions);

        var result = computeArea(p);
        System.out.println(result);

        var img = new BufferedImage(p.poly.getBounds().width + 3, p.poly.getBounds().height + 3, BufferedImage.TYPE_INT_RGB);
        var g = img.createGraphics();
        g.setColor(Color.YELLOW);
        g.draw(p.poly);
        g.dispose();
        ImageIO.write(img, "png", new File("part1.png"));
    }

    @SneakyThrows
    public static void part2() {
        var instructions = AocUtils.mapLines(inputFile, Instruction::ofLinePart2);
        var p = createPolygon(instructions);
        var result = computeArea(p);

        System.out.println(result);
    }

    private static long computeArea(PolygonContainer p) {
        var poly = p.poly;
        long area = 0;
        long missing = 0;
        int n = p.points.size();
        for (int i = 0; i < n; i++) {
            var currentPoint = p.points.get(i % n);
            var nextPoint = p.points.get((i + 1) % n);
            var mid = new Position((nextPoint.x() + currentPoint.x()) / 2, (nextPoint.y() + currentPoint.y()) / 2);

            boolean containsCurrent = poly.contains(currentPoint.x(), currentPoint.y());
            boolean containsNext = poly.contains(nextPoint.x(), nextPoint.y());
            boolean containsMid = poly.contains(mid.x(), mid.y());

            missing += containsCurrent ? 0 : 1;
            missing += containsMid ? 0 : Math.abs(currentPoint.x() - nextPoint.x()) + Math.abs(currentPoint.y() - nextPoint.y()) - 2;
            missing += containsNext ? 0 : 1;

            long delta = (long) currentPoint.x() * nextPoint.y() - (long) currentPoint.y() * nextPoint.x();
            area += delta;
        }
        return Math.abs(area / 2) + missing - 1;
    }

    private static PolygonContainer createPolygon(List<Instruction> instructions) {
        var starting = new Position(0, 0);
        int minX = 0;
        int minY = 0;
        var currentPos = starting;
        for (var in : instructions) {
            var l = in.l;
            if (in.d.x() > 0) {
                l++;
            }
            currentPos = new Position(currentPos.x() + in.d.x() * l, currentPos.y() + in.d.y() * l);
            minY = Math.min(currentPos.y(), minY);
            minX = Math.min(currentPos.x(), minX);
        }
        starting = new Position(-minX + 1, -minY + 1);
        var result = new PolygonContainer(new Polygon(), new ArrayList<>());
        currentPos = starting;
        result.poly.addPoint(currentPos.x(), currentPos.y());
        for (var in : instructions) {
            var l = in.l;
            currentPos = new Position(currentPos.x() + in.d.x() * l, currentPos.y() + in.d.y() * l);
            result.points.add(currentPos);
            result.poly.addPoint(currentPos.x(), currentPos.y());
        }
        return result;
    }

    private record Instruction(Position d, int l) {

        static Instruction ofLine(String s) {
            var row = s.split(" ");
            return new Instruction(
                    directionMapper.get(row[0]),
                    Integer.parseInt(row[1])
            );
        }

        static Instruction ofLinePart2(String s) {
            var row = s.split(" ");
            var actualInstruction = row[2].substring(1, row[2].length() - 1);

            return new Instruction(
                    directionMapper2.get(actualInstruction.charAt(actualInstruction.length() - 1)),
                    Integer.decode(actualInstruction.substring(0, actualInstruction.length() - 1))
            );
        }
    }

    private record PolygonContainer(Polygon poly, List<Position> points) {

    }

}

package ocm.ite.adventofcode.y2023;

import ocm.ite.adventofcode.AocUtils;

public class Day02 {

    public static void main(String[] args) {
        part1();
        part2();
    }

    public static void part1() {
        var lines = AocUtils.readLines("/input/2023/day02.txt");
        var result = 0;
        for (var line : lines) {
            String[] gameData = line.split(":");
            var gameId = Integer.parseInt(gameData[0].substring(5));
            var rounds = gameData[1].split(";");
            int rmax = 0, gmax = 0, bmax = 0;
            for (String round : rounds) {
                var colorInputs = round.split(",");
                for (var colorInput : colorInputs) {
                    var data = colorInput.strip().split(" ");
                    var count = Integer.parseInt(data[0]);
                    var color = data[1];
                    switch (color) {
                        case "red" -> rmax = Math.max(rmax, count);
                        case "green" -> gmax = Math.max(gmax, count);
                        case "blue" -> bmax = Math.max(bmax, count);
                    }
                }
            }
            if (rmax <= 12 && gmax <= 13 && bmax <= 14) {
                result += gameId;
            }
        }
        System.out.println(result);
    }


    public static void part2() {
        var lines = AocUtils.readLines("/input/2023/day02.txt");
        var result = 0;
        for (var line : lines) {
            String[] gameData = line.split(":");
            var rounds = gameData[1].split(";");
            int rmax = 0, gmax = 0, bmax = 0;
            for (String round : rounds) {
                var colorInputs = round.split(",");
                for (var colorInput : colorInputs) {
                    var data = colorInput.strip().split(" ");
                    var count = Integer.parseInt(data[0]);
                    var color = data[1];
                    switch (color) {
                        case "red" -> rmax = Math.max(rmax, count);
                        case "green" -> gmax = Math.max(gmax, count);
                        case "blue" -> bmax = Math.max(bmax, count);
                    }
                }
            }
            result += rmax * gmax * bmax;
        }
        System.out.println(result);
    }

}

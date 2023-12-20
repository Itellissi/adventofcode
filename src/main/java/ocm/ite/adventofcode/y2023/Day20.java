package ocm.ite.adventofcode.y2023;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import ocm.ite.adventofcode.AocUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Day20 {

    private static final String inputFile = "/input/2023/day20.txt";

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

        var modules = new HashMap<String, MagicModule>();
        var moduleMappingId = new HashMap<MagicModule, List<String>>();

        var broadcaster = new Broadcaster();
        var moduleMapping = initInputData(modules, moduleMappingId, broadcaster);

        long low = 0;
        long high = 0;
        for (int i = 0; i < 1000; i++) {
            var result = sendPulse(broadcaster, moduleMapping);
            high += result.high();
            low += result.low();
        }
        System.out.println(low + " " + high);
        System.out.println(high * low);
    }

    @SneakyThrows
    public static void part2() {
        var modules = new HashMap<String, MagicModule>();
        var moduleMappingId = new HashMap<MagicModule, List<String>>();

        var broadcaster = new Broadcaster();
        var moduleMapping = initInputData(modules, moduleMappingId, broadcaster);

        var startingFlips = modules.values()
                .stream()
                .filter(m -> m instanceof FlipFlop)
                .map(FlipFlop.class::cast)
                .filter(f -> f.input.contains(broadcaster))
                .toList();

        var detectedCycles = 0;
        var index = 0;
        while (detectedCycles < startingFlips.size()) {
            index++;
            sendPulse(broadcaster, moduleMapping);
            for (var flip : startingFlips) {
                if (flip.cycle < 0 && flip.state == (index % 2 == 0)) {
                    flip.cycle = index;
                    detectedCycles++;
                }
            }
        }

        var result = 1L;
        for (var f : startingFlips) {
            result = AocUtils.ppcm(result, f.cycle);
        }
        System.out.println(result);
    }

    private static PulseResult sendPulse(Broadcaster broadcaster, Map<MagicModule, List<MagicModule>> moduleMapping) {
        var low = 1;
        var high = 0;
        var currentLinks = moduleMapping.get(broadcaster)
                .stream()
                .map(m -> new Pulse(broadcaster, m, false))
                .collect(Collectors.toCollection(LinkedList::new));

        while (!currentLinks.isEmpty()) {
            var currentPulse = currentLinks.removeFirst();
            if (currentPulse.signal) {
                high++;
            } else {
                low++;
            }
            var currentModule = currentPulse.target;
            var newSignal = currentModule.onPulse(currentPulse);
            if (newSignal != null) {
                var newTargets = moduleMapping.get(currentModule);
                newTargets.forEach(newTarget -> currentLinks.addLast(new Pulse(currentModule, newTarget, newSignal)));
            }
        }
        return new PulseResult(low, high);
    }

    private static Map<MagicModule, List<MagicModule>> initInputData(HashMap<String, MagicModule> modules, HashMap<MagicModule, List<String>> moduleMappingId, Broadcaster broadcaster) {
        for (var line : AocUtils.readLines(inputFile)) {
            var components = line.split(" -> ");
            if (line.startsWith("broadcaster")) {
                modules.put(components[0], broadcaster);
                moduleMappingId.put(broadcaster, List.of(components[1].split(", ")));
            } else {
                var moduleName = components[0].substring(1);
                var moduleType = components[0].charAt(0);
                var module = moduleType == '%' ? new FlipFlop(moduleName) : new Conjunction(moduleName);
                modules.put(moduleName, module);
                moduleMappingId.put(module, List.of(components[1].split(", ")));
            }
        }

        var moduleMapping = moduleMappingId.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream()
                                .map(n -> modules.getOrDefault(n, NullModule.of(n)))
                                .collect(Collectors.toList())
                ));

        moduleMapping.forEach(
                (input, output) -> output.stream()
                        .filter(o -> o instanceof Conjunction)
                        .map(Conjunction.class::cast)
                        .forEach(c -> c.memory.put(input, false))
        );

        moduleMapping.forEach(
                (input, output) -> output.stream()
                        .filter(o -> o instanceof FlipFlop)
                        .map(FlipFlop.class::cast)
                        .forEach(c -> c.input.add(input))
        );
        return moduleMapping;
    }

    private interface MagicModule {

        Boolean onPulse(Pulse p);

        String getModuleName();
    }

    private record PulseResult(int low, int high) {
    }

    private static class Broadcaster implements MagicModule {

        @Override
        public Boolean onPulse(Pulse p) {
            return false;
        }

        @Override
        public String getModuleName() {
            return "broadcaster";
        }

    }

    // if receives false
    @RequiredArgsConstructor
    private static class FlipFlop implements MagicModule {

        @Getter
        private final String moduleName;

        private final Set<MagicModule> input = new HashSet<>();

        @Getter
        private boolean state;

        @Getter
        @Setter
        private long cycle = -1;

        @Override
        public Boolean onPulse(Pulse p) {
            if (p.signal) {
                return null;
            }
            state = !state;
            return state;
        }

    }

    @RequiredArgsConstructor
    private static class Conjunction implements MagicModule {

        @Getter
        private final String moduleName;

        private final Map<MagicModule, Boolean> memory = new HashMap<>();

        @Override
        public Boolean onPulse(Pulse p) {
            memory.put(p.source, p.signal);
            return memory.values().stream().anyMatch(b -> !b);
        }

    }


    @RequiredArgsConstructor
    private static class NullModule implements MagicModule {

        @Getter
        private final String moduleName;

        private static NullModule of(String moduleName) {
            return new NullModule(moduleName);
        }

        @Override
        public Boolean onPulse(Pulse p) {
            return null;
        }
    }

    private record Pulse(MagicModule source, MagicModule target, boolean signal) {

    }
}

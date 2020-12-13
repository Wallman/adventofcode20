package day13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day13 {

    public static long run(Input input) {
        List<BusCadence> busesInSeries = findBusesInSeries(input.buses);
        Long lowestCommonDenominator = busesInSeries.stream()
                .map(bc -> (long) bc.busId)
                .reduce(1L, (i1, i2) -> i1 * i2);

        long i = lowestCommonDenominator;

        while (!allBusesMatch(i, input.buses, busesInSeries)) {
            i += lowestCommonDenominator;
            if (i % (lowestCommonDenominator * 1000000) == 0) {
                System.out.println(i);
            }
        }
        return i;
    }

    private static List<BusCadence> findBusesInSeries(List<BusCadence> buses) {
        return buses.stream()
                .filter(busCadence -> buses.stream()
                        .anyMatch(busCadence1 -> busCadence1.delay == busCadence.busId || busCadence1.busId == busCadence.delay))
                .collect(Collectors.toList());
    }

    private static boolean allBusesMatch(long minute, List<BusCadence> allBuses, List<BusCadence> busesInSeries) {
        return busesInSeries.stream()
                .map(bc -> bc.delay)
                .anyMatch(delay -> {
                    boolean match = isMatch(minute - delay, allBuses);
                    if (match) {
                        System.out.println(delay);
                    }
                    return match;
                });
    }

    private static boolean isMatch(long minute, List<BusCadence> allBuses) {
        return allBuses.stream()
                .allMatch(busCadence -> (minute + busCadence.delay) % busCadence.busId == 0);
    }

    public static Input input() {
        try {
            List<String> strings = Files.readAllLines(Path.of("src/day13/day13.txt"));
            int earliestMinute = Integer.parseInt(strings.get(0));
            List<String> buses = Arrays.asList(strings.get(1).split(","));
            ArrayList<BusCadence> busCadences = new ArrayList<>();
            for (int i = 0; i < buses.size(); i++) {
                String busId = buses.get(i);
                if (!busId.equals("x")) {
                    busCadences.add(new BusCadence(
                            Integer.parseInt(busId),
                            i
                    ));
                }
            }
            return new Input(earliestMinute, busCadences);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    static class Input {
        int earliestMinute;
        List<BusCadence> buses;

        public Input(int earliestMinute, List<BusCadence> buses) {
            this.earliestMinute = earliestMinute;
            this.buses = buses;
        }
    }

    static class BusCadence {
        int busId;
        int delay;

        public BusCadence(int busId, int delay) {
            this.busId = busId;
            this.delay = delay;
        }
    }
}

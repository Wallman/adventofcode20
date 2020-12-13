package day13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day13 {

    public static long run(Input input) {
        BusCadence max = input.buses.stream()
                .max(Day13::compare)
                .orElseThrow(RuntimeException::new);
        long i = findStart(max);

        while (!allBusesMatch(i, input.buses)) {
            i += max.busId;
            if (i % 1000000L == 0) {
                System.out.println(i);
            }
        }
        return i;
    }

    private static long findStart(BusCadence max) {
        long i = 100000000000000L;
        while ((i + max.delay) % max.busId != 0) {
            i++;
        }
        System.out.println("Start: " + i);
        return i;
    }

    private static boolean allBusesMatch(long minute, List<BusCadence> buses) {
        return buses.stream()
                .allMatch(busCadence -> (minute + busCadence.delay) % busCadence.busId == 0);
    }

    private static int compare(BusCadence bc1, BusCadence bc2) {
        return bc1.busId - bc2.busId;
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

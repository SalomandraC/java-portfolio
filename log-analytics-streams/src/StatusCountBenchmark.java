import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StatusCountBenchmark {
    private StatusCountBenchmark() { }

    public static void main(String[] args) {
        List<LogRecord> data = generate(10_000_000);
        for (int i = 0; i < 4; i++) {
            Queries.statusCountImperative(data);
            Queries.statusCount(data);
        }

        long start = System.nanoTime();
        Map<Integer, Long> imp = Queries.statusCountImperative(data);
        long timeImp = System.nanoTime() - start;

        start = System.nanoTime();
        Map<Integer, Long> str = Queries.statusCount(data);
        long timeStr = System.nanoTime() - start;

        start = System.nanoTime();
        Map<Integer, Long> strParall = Queries.statusCountParal(data);
        long timeStrParall = System.nanoTime() - start;

        System.out.printf("Императивно: %,d наносекунд %n", timeImp, timeImp / 1_000_000.0);
        System.out.printf("Стрим: %,d наносекунд %n", timeStr, timeStr / 1_000_000.0);
        System.out.printf("СтримПараллельно: %,d наносекунд %n", timeStrParall, timeStrParall / 1_000_000.0);
        System.out.printf("Стрим / императив: %.2fx%n", (double) timeStr / timeImp);
        System.out.printf("СтримПарал / императив: %.2fx%n", (double) timeStrParall / timeImp);
        System.out.printf("Стрим / СтримПарал: %.2fx%n", (double) timeStrParall / timeStr);
    }

    private static List<LogRecord> generate(int n) {
        Random rnd = new Random(42);
        int[] statuses = {200, 201, 403, 404, 500, 503};
        List<LogRecord> list = new ArrayList<>(n);

        LocalDateTime base = LocalDateTime.of(2026, 9, 1, 0, 0);

        for (int i = 0; i < n; i++) {
            list.add(new LogRecord(
                    base.plusSeconds(i),
                    "10.0.0." + (rnd.nextInt(254) + 1),
                    rnd.nextBoolean() ? LogType.GET : LogType.POST,
                    "/api/p" + rnd.nextInt(100),
                    statuses[rnd.nextInt(statuses.length)],
                    rnd.nextInt(50_000)
            ));
        }
        return list;
    }

}

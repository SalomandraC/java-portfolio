import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.*;


public class QueriesTest {
    private final List<LogRecord> data = List.of(
            new LogRecord(LocalDateTime.of(2026, 9, 1, 8, 0),
                    "10.0.0.1", LogType.GET,  "/b",     404, 200),
            new LogRecord(LocalDateTime.of(2026, 9, 1, 9, 0),
                    "10.0.0.2", LogType.GET,  "/a",     500, 200),
            new LogRecord(LocalDateTime.of(2026, 9, 1, 10, 0),
                    "10.0.0.3", LogType.GET,  "/c",     500, 500),
            new LogRecord(LocalDateTime.of(2026, 9, 1, 11, 0),
                    "10.0.0.4", LogType.GET,  "/d",     200, 999),
            new LogRecord(LocalDateTime.of(2026, 9, 1, 12, 0),
                    "10.0.0.1", LogType.POST, "/login", 200, 100)
    );

    @Test
    // сортировка по size убыв., при равенстве — по path возр
    void topErrorSort() {
        List<LogRecord> top = Queries.topError(data);
        assertEquals(3, top.size());

        assertEquals("/c",  top.get(0).getPath());
        assertEquals(500,   top.get(0).getSize());

        assertEquals("/a",  top.get(1).getPath());
        assertEquals(200,   top.get(1).getSize());
        assertEquals("/b",  top.get(2).getPath());
        assertEquals(200,   top.get(2).getSize());
    }

    @Test
    // возвращается не более 10 записей
    void topErrorLimits() {
        List<LogRecord> many = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            many.add(new LogRecord(
                    LocalDateTime.of(2026, 9, 1, 0, 0).plusMinutes(i),
                    "10.0.0.1", LogType.GET, "/p" + i, 500, i));
        }

        assertEquals(10, Queries.topError(many).size());
    }

    @Test
    @DisplayName("Q2-императивно: количество запросов по каждому статусу")
    void statusCountImp() {
        Map<Integer, Long> expected = Map.of(
                200, 2L,
                404, 1L,
                500, 2L
        );

        assertEquals(expected, Queries.statusCountImperative(data));
    }

    @Test
    @DisplayName("Q2-стрим: количество запросов по каждому статусу")
    void statusCountStr() {
        Map<Integer, Long> expected = Map.of(
                200, 2L,
                404, 1L,
                500, 2L
        );

        assertEquals(expected, Queries.statusCount(data));
    }

    @Test
    void statusCount() {
        assertEquals(
                Queries.statusCountImperative(data),
                Queries.statusCount(data)
        );
    }

    @Test
    void ipPathsGroupsIp() {
        Map<String, Set<String>> byIp = Queries.ipPaths(data);
        assertEquals(4, byIp.size());
        assertEquals(Set.of("/b", "/login"), byIp.get("10.0.0.1"));
        assertEquals(Set.of("/a"), byIp.get("10.0.0.2"));
        assertEquals(Set.of("/c"), byIp.get("10.0.0.3"));
        assertEquals(Set.of("/d"), byIp.get("10.0.0.4"));
    }

    @Test
    @DisplayName("Q4: sizeStats считает count/sum/min/max/average")
    void sizeStatsAggregates() {
        IntSummaryStatistics stats = Queries.sizeStats(data);

        assertEquals(5,                            stats.getCount());
        assertEquals(200 + 200 + 500 + 999 + 100,  stats.getSum());   // 1999
        assertEquals(100,                          stats.getMin());
        assertEquals(999,                          stats.getMax());
        assertEquals(1999.0 / 5,                   stats.getAverage(), 1e-9);
    }

    @Test
    void firstRequestReturns() {
        Optional<LogRecord> first = Queries.firstRequest(data);

        assertTrue(first.isPresent());
        assertEquals(LocalDateTime.of(2026, 9, 1, 8, 0),
                first.get().getTimestamp());
        assertEquals("10.0.0.1", first.get().getIp());
        assertEquals("/b",       first.get().getPath());
    }

    @Test
    void firstRequestOnEmptyList() {
        Optional<LogRecord> first = Queries.firstRequest(List.of());
        assertFalse(first.isPresent());
    }

    @Test
    void uniquePathsDistinct() {
        List<String> paths = Queries.uniquePaths(data);
        assertEquals(
                List.of("/a", "/b", "/c", "/d", "/login"),
                paths
        );
    }

    @Test
    void uniquePathsRemovesDupli() {
        List<LogRecord> withDupes = List.of(
                new LogRecord(LocalDateTime.of(2026, 9, 1, 8, 0),
                        "1.1.1.1", LogType.GET, "/x", 200, 10),
                new LogRecord(LocalDateTime.of(2026, 9, 1, 9, 0),
                        "1.1.1.2", LogType.GET, "/x", 200, 10),
                new LogRecord(LocalDateTime.of(2026, 9, 1, 10, 0),
                        "1.1.1.3", LogType.GET, "/y", 200, 10)
        );

        assertEquals(List.of("/x", "/y"), Queries.uniquePaths(withDupes));
    }
}

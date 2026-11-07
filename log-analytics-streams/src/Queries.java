import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Queries {

    //Топ-10 ошибочных запросов (status >= 400) по размеру ответа.
    static List<LogRecord> topError(List<LogRecord> records) {
        return records.stream()
                .filter(r -> r.getStatus() >= 400)
                .sorted(Comparator
                        .comparingInt(LogRecord::getSize).reversed()
                        .thenComparing(LogRecord::getPath))
                .limit(10)
                .collect(Collectors.toList());
    }

    //Количество запросов по каждому коду состояния.
    // Императивно
    static Map<Integer, Long> statusCountImperative (List<LogRecord> records) {
        Map<Integer, Long> statusMap = new HashMap<>();
        for(LogRecord record : records) {
            if (statusMap.containsKey(record.getStatus())){
                statusMap.put(record.getStatus(), statusMap.get(record.getStatus()) + 1);
            }
            else {
                statusMap.put(record.getStatus(), 1L);
            }
        }
        return statusMap;
    }

    // Стриммеративно
    static Map<Integer, Long> statusCount (List<LogRecord> records) {
        return records.stream()
                .collect(Collectors.groupingBy(
                        LogRecord::getStatus,
                        Collectors.counting())
                );
    }

    static Map<Integer, Long> statusCountParal (List<LogRecord> records) {
        return records.stream()
                .parallel()
                .collect(Collectors.groupingBy(
                        LogRecord::getStatus,
                        Collectors.counting())
                );
    }

    // По каждому IP — список путей, к которым обращались.
    static Map<String, Set<String>> ipPaths (List<LogRecord> records) {
        return records.stream()
                .parallel()
                .collect(Collectors.groupingBy(
                        LogRecord::getIp,
                        Collectors.mapping(LogRecord::getPath,
                                Collectors.toSet())));
    }

    // самый первый запрос лога (по timestamp)
    static Optional<LogRecord> firstRequest(List<LogRecord> records) {
        return records.stream()
                .min(Comparator.comparing(LogRecord::getTimestamp));
    }

    // Статистика по размерам ответов;
    static IntSummaryStatistics sizeStats(List<LogRecord> records) {
        return records.stream()
                .parallel()
                .mapToInt(LogRecord::getSize)
                .summaryStatistics();
    }

    // Объединение логов из нескольких файлов
    static List<LogRecord> loadRecords (List<Path> files) {
        return (List<LogRecord>) files.stream()
                .flatMap( file -> {
                        try {
                            return LogLoader.load(file).stream();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                })
                .toList();
    }

    // уникальные пути в логе, по алфавиту
    static List<String> uniquePaths(List<LogRecord> records) {
        return records.stream()
                .map(LogRecord::getPath)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}

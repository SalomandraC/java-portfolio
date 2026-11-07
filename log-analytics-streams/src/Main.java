import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.*;

public final class Main {
    private Main() { }

    public static void main(String[] args) throws IOException {
        List<LogRecord> records = LogLoader.load(Path.of("05-server-logs.csv"));

        System.out.println("== Q1. Топ-10 ошибочных запросов (status >= 400) по размеру ответа. ==");
        List<LogRecord> top10Rec = Queries.topError(records);
        for (LogRecord r : top10Rec) {
            System.out.printf("  %-15s %-20s %3d  %6d%n",
                    r.getIp(), r.getPath(), r.getStatus(), r.getSize());
        }
        System.out.println();

        System.out.println("== Q2. Количество запросов по каждому коду состояния / Императивно ==");
        Map<Integer, Long> imperativePotato = Queries.statusCountImperative(records);
        imperativePotato.forEach((first, second) -> IO.println(first + ": " + second));
        IO.println();

        System.out.println("== Q2. Количество запросов по каждому коду состояния / Стриммеративно ==");
        Map<Integer, Long> justPotato = Queries.statusCount(records);
        justPotato.forEach((first, second) -> IO.println(first + ": " + second));
        System.out.println();

        System.out.println("== Q3. По каждому IP — список путей, к которым обращались. ==");
        Map<String, Set<String>> ipPath = Queries.ipPaths(records);
        ipPath.entrySet().stream().forEach(
                (first) -> {
                    IO.println(first + ":");
                    first.getValue().forEach(el -> {
                        IO.println("\t" + el);
                    });
                    IO.println();
                }
        );

        System.out.println("== Q4. самый первый запрос лога ==");
        Optional<LogRecord> first = Queries.firstRequest(records);
        LogRecord r = first.get();
        IO.println(r.toString());

        System.out.println("== Q4. Статистика по размерам ответов ==");
        System.out.println("  " + Queries.sizeStats(records));
        System.out.println();

        System.out.println("== Q5. Уникальные пути по алфавиту ==");
        for (String path : Queries.uniquePaths(records)) {
            System.out.println("  " + path);
        }

        System.out.println("== Q5-extra. Объединение логов из нескольких файлов ==");

        List<Path> files = List.of(Path.of("05-server-logs_merge_1.csv"), Path.of("05-server-logs_merge_2.csv"));

        List<LogRecord> merged = Queries.loadRecords(files);
        System.out.printf("\tвсего записей после объединения: %d%n", merged.size());

        for (LogRecord p : merged) {
            System.out.printf("  %s  %-15s %-20s %3d  %6d%n",
                    p.getTimestamp(), p.getIp(), p.getPath(),
                    p.getStatus(), p.getSize());
        }
        System.out.println();
    }
}

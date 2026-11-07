
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class LogLoader {
    LogLoader() { }
    static LogRecord parse(String line) {
        String[] f = line.split(",");
        if (f.length != 6) {
            return null;
        }
        try {
            return new LogRecord(
                    LocalDateTime.parse(f[0].trim()),
                    f[1].trim(),
                    LogType.valueOf(f[2].trim()),
                    f[3].trim(),
                    Integer.parseInt(f[4].trim()),
                    Integer.parseInt(f[5].trim())
            );
        } catch (RuntimeException e) {
            return null;
        }
    }

    static List<LogRecord> load(Path file)
            throws IOException {
        try (Stream<String> lines = Files.lines(file, StandardCharsets.UTF_8)) {
            return lines
                    .skip(1)
                    .map(line -> LogLoader.parse(line))
                    .filter(x -> Objects.nonNull(x))
                    .collect(Collectors.toList());
        }
    }

}

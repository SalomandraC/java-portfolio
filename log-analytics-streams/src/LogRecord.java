import java.time.LocalDateTime;
import java.util.Objects;

public class LogRecord {
    private final LocalDateTime timestamp;
    private final String ip;
    private final LogType method;
    private final String path;
    private final int status;
    private final int size;

    public LogRecord(LocalDateTime timestamp, String ip, LogType method,
                     String path, int status, int size) {
        this.timestamp = timestamp;
        this.ip = ip;
        this.method = method;
        this.path = path;
        this.status = status;
        this.size = size;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getIp() {
        return ip;
    }

    public LogType getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public int getStatus() {
        return status;
    }

    public int getSize() {
        return size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, ip, method, path, status, size);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LogRecord o = (LogRecord) obj;
        return status == o.status
                && size == o.size
                && Objects.equals(timestamp, o.timestamp)
                && Objects.equals(ip, o.ip)
                && method == o.method
                && Objects.equals(path, o.path);
    }

    @Override
    public String toString() {
        return "LogRecord: " +
                "timestamp = " + timestamp +
                ", ip = '" + ip + '\'' +
                ", method = " + method +
                ", path = '" + path + '\'' +
                ", status = " + status +
                ", size = " + size;
    }
}
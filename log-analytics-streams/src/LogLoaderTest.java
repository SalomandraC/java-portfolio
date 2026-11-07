import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNull;

class LogLoaderTest {
    @Test
    void parseSkipsBrokenLines() {
        assertNull(LogLoader.parse("grab"));
        assertNull(LogLoader.parse("a,b,c"));
        LogRecord r = LogLoader.parse(
                "2026-09-01T08:47:19,192.168.1.14,GET,/api/items/42,404,512");
        Assertions.assertEquals(404, r.getStatus());
    }
}
package patterns.templatemethod;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataExporterTest {
    private final List<String> rows = List.of("a", "b");

    @Test
    void csv() {
        assertEquals("value\na\nb\n", new CsvExporter().export(rows));
    }

    @Test
    void json() {
        assertEquals("[\"a\",\"b\"]", new JsonExporter().export(rows));
    }

    @Test
    void emptyInput() {
        assertEquals("[]", new JsonExporter().export(List.of()));
    }
}

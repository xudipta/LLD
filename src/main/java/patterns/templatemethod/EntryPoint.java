package patterns.templatemethod;

import java.util.List;

public class EntryPoint {
    public static void main(String[] args) {
        List<String> rows = List.of("a", "b", "c");
        System.out.print(new CsvExporter().export(rows));
        System.out.println(new JsonExporter().export(rows));
    }
}

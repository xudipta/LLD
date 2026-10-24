package patterns.templatemethod;

public class CsvExporter extends DataExporter {
    @Override
    protected String header() { return "value\n"; }

    @Override
    protected String formatRow(String row, boolean last) { return row + "\n"; }
}

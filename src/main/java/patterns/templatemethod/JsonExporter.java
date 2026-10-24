package patterns.templatemethod;

public class JsonExporter extends DataExporter {
    @Override
    protected String header() { return "["; }

    @Override
    protected String formatRow(String row, boolean last) {
        return "\"" + row + "\"" + (last ? "" : ",");
    }

    @Override
    protected String footer() { return "]"; }
}

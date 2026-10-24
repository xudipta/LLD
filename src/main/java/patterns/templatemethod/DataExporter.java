package patterns.templatemethod;

import java.util.List;

public abstract class DataExporter {
    /** The template: fixed skeleton, subclasses fill in the steps. */
    public final String export(List<String> rows) {
        StringBuilder out = new StringBuilder(header());
        for (int i = 0; i < rows.size(); i++) {
            out.append(formatRow(rows.get(i), i == rows.size() - 1));
        }
        return out.append(footer()).toString();
    }

    protected abstract String formatRow(String row, boolean last);

    /** Hooks with a default of "nothing". */
    protected String header() { return ""; }

    protected String footer() { return ""; }
}

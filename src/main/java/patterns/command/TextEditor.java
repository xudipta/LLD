package patterns.command;

public class TextEditor {
    private final StringBuilder text = new StringBuilder();

    public void append(String s) {
        text.append(s);
    }

    public void truncateTo(int length) {
        text.setLength(length);
    }

    public int length() {
        return text.length();
    }

    public String getText() {
        return text.toString();
    }
}

package patterns.command;

public class AppendCommand implements Command {
    private final TextEditor editor;
    private final String suffix;
    private int lengthBefore;

    public AppendCommand(TextEditor editor, String suffix) {
        this.editor = editor;
        this.suffix = suffix;
    }

    @Override
    public void execute() {
        lengthBefore = editor.length();
        editor.append(suffix);
    }

    @Override
    public void undo() {
        editor.truncateTo(lengthBefore);
    }
}

package patterns.command;

public class EntryPoint {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        CommandHistory history = new CommandHistory();
        history.run(new AppendCommand(editor, "Hello"));
        history.run(new AppendCommand(editor, " World"));
        System.out.println(editor.getText());
        history.undo();
        System.out.println(editor.getText());
    }
}

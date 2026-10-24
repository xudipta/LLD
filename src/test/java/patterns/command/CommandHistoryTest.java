package patterns.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandHistoryTest {
    @Test
    void undoRevertsTheLastCommandOnly() {
        TextEditor editor = new TextEditor();
        CommandHistory history = new CommandHistory();
        history.run(new AppendCommand(editor, "Hello"));
        history.run(new AppendCommand(editor, " World"));
        assertTrue(history.undo());
        assertEquals("Hello", editor.getText());
    }

    @Test
    void undoAllReturnsToEmpty() {
        TextEditor editor = new TextEditor();
        CommandHistory history = new CommandHistory();
        history.run(new AppendCommand(editor, "a"));
        history.run(new AppendCommand(editor, "b"));
        history.undo();
        history.undo();
        assertEquals("", editor.getText());
    }

    @Test
    void undoWithNothingToUndoReturnsFalse() {
        assertFalse(new CommandHistory().undo());
    }
}

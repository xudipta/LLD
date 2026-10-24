package patterns.command;

import java.util.ArrayDeque;
import java.util.Deque;

public class CommandHistory {
    private final Deque<Command> done = new ArrayDeque<>();

    public void run(Command command) {
        command.execute();
        done.push(command);
    }

    /** @return false when there is nothing to undo */
    public boolean undo() {
        if (done.isEmpty()) {
            return false;
        }
        done.pop().undo();
        return true;
    }
}

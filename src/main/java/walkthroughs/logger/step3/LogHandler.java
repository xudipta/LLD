package walkthroughs.logger.step3;

import java.io.PrintStream;

/**
 * STEP 3 - Chain of Responsibility. Each handler owns one level and its own destination
 * (say ERROR to stderr, INFO to stdout) and passes everything else along.
 */
public class LogHandler {
    private final Level level;
    private final PrintStream sink;
    private final LogHandler next;

    public LogHandler(Level level, PrintStream sink, LogHandler next) {
        this.level = level;
        this.sink = sink;
        this.next = next;
    }

    public void handle(Level incoming, String msg) {
        if (incoming == level) {
            sink.println(level + ": " + msg);
        } else if (next != null) {
            next.handle(incoming, msg);
        }
    }
}

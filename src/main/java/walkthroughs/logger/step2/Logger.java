package walkthroughs.logger.step2;

import java.io.PrintStream;

/**
 * STEP 2 - an enum replaces the magic numbers and the if/else chain, and the destination is injected.
 * A new level is a new enum constant. But every level still goes to the same place.
 */
public class Logger {
    private final PrintStream out;

    public Logger(PrintStream out) {
        this.out = out;
    }

    public void log(Level level, String msg) {
        out.println(level + ": " + msg);
    }
}

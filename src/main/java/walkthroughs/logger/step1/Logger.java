package walkthroughs.logger.step1;

/** STEP 1 - one method, an if/else per level, output hard-wired to the console. */
public class Logger {
    public static final int DEBUG = 1;
    public static final int ERROR = 2;
    public static final int INFO = 3;

    public void log(int level, String msg) {
        if (level == 1) {
            System.out.println("DEBUG: " + msg);
        } else if (level == 2) {
            System.out.println("ERROR: " + msg);
        } else if (level == 3) {
            System.out.println("INFO: " + msg);
        }
    }
}

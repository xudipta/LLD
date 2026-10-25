package walkthroughs.logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Same scenarios, every step. Levels are 1 = DEBUG, 2 = ERROR, 3 = INFO as in the original. */
class BehaviourTest {
    interface Log {
        void log(int level, String msg);
    }

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream captured;

    @BeforeEach
    void capture() {
        captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured));
    }

    @AfterEach
    void restore() {
        System.setOut(originalOut);
    }

    private String output() {
        return captured.toString().replace("\r", "").trim();
    }

    // ---- adapters ----
    static Stream<org.junit.jupiter.params.provider.Arguments> steps() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of("step 1: if/else", (Function<PrintStream, Log>) out -> {
                    walkthroughs.logger.step1.Logger l = new walkthroughs.logger.step1.Logger();
                    return l::log;                      // step 1 can only print to System.out, which the test captures
                }),
                org.junit.jupiter.params.provider.Arguments.of("step 2: enum and injected output", (Function<PrintStream, Log>) out -> {
                    walkthroughs.logger.step2.Logger l = new walkthroughs.logger.step2.Logger(out);
                    return (level, msg) -> l.log(walkthroughs.logger.step2.Level.values()[level - 1], msg);
                }),
                org.junit.jupiter.params.provider.Arguments.of("step 3: chain of responsibility", (Function<PrintStream, Log>) out -> {
                    var chain = new walkthroughs.logger.step3.LogHandler(walkthroughs.logger.step3.Level.INFO, out,
                            new walkthroughs.logger.step3.LogHandler(walkthroughs.logger.step3.Level.DEBUG, out,
                                    new walkthroughs.logger.step3.LogHandler(walkthroughs.logger.step3.Level.ERROR, out, null)));
                    return (level, msg) -> chain.handle(walkthroughs.logger.step3.Level.values()[level - 1], msg);
                }));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("steps")
    void debugMessagesAreLabelled(String step, Function<PrintStream, Log> factory) {
        factory.apply(System.out).log(1, "hello");
        assertEquals("DEBUG: hello", output());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("steps")
    void errorMessagesAreLabelled(String step, Function<PrintStream, Log> factory) {
        factory.apply(System.out).log(2, "boom");
        assertEquals("ERROR: boom", output());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("steps")
    void infoMessagesAreLabelled(String step, Function<PrintStream, Log> factory) {
        factory.apply(System.out).log(3, "fyi");
        assertEquals("INFO: fyi", output());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("steps")
    void messagesComeOutInOrder(String step, Function<PrintStream, Log> factory) {
        Log log = factory.apply(System.out);
        log.log(3, "one");
        log.log(1, "two");
        log.log(2, "three");
        assertEquals("INFO: one\nDEBUG: two\nERROR: three", output());
    }

    /** Only step 3 can do this without changing the logger's code. */
    @Test
    void step3CanSendEachLevelToItsOwnDestination() {
        ByteArrayOutputStream errors = new ByteArrayOutputStream();
        ByteArrayOutputStream everythingElse = new ByteArrayOutputStream();
        var chain = new walkthroughs.logger.step3.LogHandler(walkthroughs.logger.step3.Level.ERROR, new PrintStream(errors),
                new walkthroughs.logger.step3.LogHandler(walkthroughs.logger.step3.Level.INFO, new PrintStream(everythingElse), null));
        chain.handle(walkthroughs.logger.step3.Level.ERROR, "disk full");
        chain.handle(walkthroughs.logger.step3.Level.INFO, "started");
        assertEquals("ERROR: disk full", errors.toString().trim());
        assertEquals("INFO: started", everythingElse.toString().trim());
    }
}

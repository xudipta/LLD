package problems.logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogProcessorTest {
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream captured;
    private LogProcessor chain;

    @BeforeEach
    void setUp() {
        captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured));
        chain = new InfoLogProcessor(new DebugLogProcessor(new ErrorLogProcessor(null)));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private String output() {
        return captured.toString().trim();
    }

    @Test
    void routesInfo() {
        chain.log(LogProcessor.INFO, "hello");
        assertEquals("INFO: hello", output());
    }

    @Test
    void routesDebugThroughInfoHandler() {
        chain.log(LogProcessor.DEBUG, "hello");
        assertEquals("DEBUG: hello", output());
    }

    @Test
    void routesErrorToLastHandler() {
        chain.log(LogProcessor.ERROR, "hello");
        assertEquals("ERROR: hello", output());
    }

    @Test
    void unknownLevelIsSilentlyDropped() {
        chain.log(99, "hello");
        assertEquals("", output());
    }
}

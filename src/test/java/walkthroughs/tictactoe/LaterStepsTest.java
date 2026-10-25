package walkthroughs.tictactoe;

import org.junit.jupiter.api.Test;
import walkthroughs.tictactoe.step2.Outcome;
import walkthroughs.tictactoe.step4.ConsoleRunner;
import walkthroughs.tictactoe.step4.Player;
import walkthroughs.tictactoe.step4.TicTacToe;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** What the later steps can do that step 1 cannot. */
class LaterStepsTest {
    @Test
    void step2SupportsOtherBoardSizes() {
        walkthroughs.tictactoe.step2.TicTacToe g = new walkthroughs.tictactoe.step2.TicTacToe(4);
        int[][] moves = {{0, 0}, {1, 0}, {0, 1}, {1, 1}, {0, 2}, {1, 2}};
        for (int[] m : moves) assertEquals(Outcome.CONTINUE, g.move(m[0], m[1]));   // three in a row is not enough on 4x4
        assertEquals(Outcome.X_WINS, g.move(0, 3));
    }

    @Test
    void step4SupportsOtherBoardSizesAndMorePlayers() {
        TicTacToe g = new TicTacToe(3, List.of(new Player("A", 'A'), new Player("B", 'B'), new Player("C", 'C')));
        assertEquals("A", g.currentPlayer().name());
        g.move(0, 0);
        assertEquals("B", g.currentPlayer().name());
        g.move(1, 1);
        assertEquals("C", g.currentPlayer().name());
    }

    private String runWithInput(String input, TicTacToe game, ByteArrayOutputStream captured) {
        return new ConsoleRunner(game, new ByteArrayInputStream(input.getBytes()), new PrintStream(captured)).run();
    }

    @Test
    void consoleRunnerPlaysAWholeGameFromScriptedInput() {
        TicTacToe game = new TicTacToe(3, List.of(new Player("Asha", 'X'), new Player("Ben", 'O')));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String winner = runWithInput("0 0\n1 0\n0,1\n1,1\n0 2\n", game, out);
        assertEquals("Asha", winner);
        assertTrue(out.toString().contains("X  X  X") || out.toString().contains(" X  X  X "));
    }

    @Test
    void consoleRunnerSurvivesGarbageInputAndIllegalMoves() {
        TicTacToe game = new TicTacToe(3, List.of(new Player("Asha", 'X'), new Player("Ben", 'O')));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // garbage, a lone number, an occupied square, then a normal game
        String winner = runWithInput("abc\n7\n0 0\n0 0\n1 0\n0 1\n1 1\n0 2\n", game, out);
        assertEquals("Asha", winner);
        String text = out.toString();
        assertTrue(text.contains("Please enter two numbers"));
        assertTrue(text.contains("Illegal move"));
    }

    @Test
    void consoleRunnerReportsATie() {
        TicTacToe game = new TicTacToe(3, List.of(new Player("Asha", 'X'), new Player("Ben", 'O')));
        String winner = runWithInput("0 0\n0 1\n0 2\n1 1\n1 0\n1 2\n2 1\n2 0\n2 2\n", game, new ByteArrayOutputStream());
        assertEquals("tie", winner);
    }
}

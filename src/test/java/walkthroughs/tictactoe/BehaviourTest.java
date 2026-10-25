package walkthroughs.tictactoe;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The same scenarios run against every step of the refactoring. The code changes a lot between
 * steps; this behaviour must not. That is what makes a refactoring safe.
 */
class BehaviourTest {
    /** Common shape for all steps; results are normalised to INVALID, CONTINUE, DRAW or the winner's symbol. */
    interface Game {
        String move(int r, int c);
    }

    private final PrintStream originalOut = System.out;

    @BeforeEach
    void silenceStep1Printing() {
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
    }

    @AfterEach
    void restoreOut() {
        System.setOut(originalOut);
    }

    // ---- adapters, one per step ----
    static Game step1() {
        walkthroughs.tictactoe.step1.TicTacToe g = new walkthroughs.tictactoe.step1.TicTacToe();
        return g::move;
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> steps() {
        return Stream.of(
                named("step 1: first version", BehaviourTest::step1),
                named("step 2: Board class", BehaviourTest::step2Adapter),
                named("step 3: players and turn queue", BehaviourTest::step3),
                named("step 4: rules separated from I/O", BehaviourTest::step4));
    }

    private static org.junit.jupiter.params.provider.Arguments named(String name, Supplier<Game> factory) {
        return org.junit.jupiter.params.provider.Arguments.of(name, factory);
    }

    static Game step2Adapter() {
        walkthroughs.tictactoe.step2.TicTacToe g = new walkthroughs.tictactoe.step2.TicTacToe(3);
        return (r, c) -> {
            walkthroughs.tictactoe.step2.Outcome o = g.move(r, c);
            return switch (o) {
                case X_WINS -> "X";
                case O_WINS -> "O";
                default -> o.name();
            };
        };
    }

    static Game step3() {
        var g = new walkthroughs.tictactoe.step3.TicTacToe(3, List.of(
                new walkthroughs.tictactoe.step3.Player("X", 'X'), new walkthroughs.tictactoe.step3.Player("O", 'O')));
        return (r, c) -> {
            var result = g.move(r, c);
            return result.outcome() == walkthroughs.tictactoe.step3.MoveResult.Outcome.WIN
                    ? result.winner().name() : result.outcome().name();
        };
    }

    static Game step4() {
        var g = new walkthroughs.tictactoe.step4.TicTacToe(3, List.of(
                new walkthroughs.tictactoe.step4.Player("X", 'X'), new walkthroughs.tictactoe.step4.Player("O", 'O')));
        return (r, c) -> {
            var result = g.move(r, c);
            return result.outcome() == walkthroughs.tictactoe.step4.MoveResult.Outcome.WIN
                    ? result.winner().name() : result.outcome().name();
        };
    }

    /** Plays the moves in order and returns the result of the last one. */
    private static String play(Supplier<Game> factory, int... rc) {
        Game game = factory.get();
        String last = null;
        for (int i = 0; i < rc.length; i += 2) {
            last = game.move(rc[i], rc[i + 1]);
        }
        return last;
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("steps")
    void gameContinuesAfterAnOrdinaryMove(String step, Supplier<Game> game) {
        assertEquals("CONTINUE", play(game, 1, 1));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("steps")
    void firstPlayerWinsARow(String step, Supplier<Game> game) {
        assertEquals("X", play(game, 0, 0, 1, 0, 0, 1, 1, 1, 0, 2));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("steps")
    void secondPlayerWinsAColumn(String step, Supplier<Game> game) {
        assertEquals("O", play(game, 0, 0, 0, 1, 1, 0, 1, 1, 2, 2, 2, 1));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("steps")
    void diagonalWins(String step, Supplier<Game> game) {
        assertEquals("X", play(game, 0, 0, 0, 1, 1, 1, 0, 2, 2, 2));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("steps")
    void antiDiagonalWins(String step, Supplier<Game> game) {
        assertEquals("X", play(game, 0, 2, 0, 0, 1, 1, 0, 1, 2, 0));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("steps")
    void fullBoardWithNoWinnerIsADraw(String step, Supplier<Game> game) {
        assertEquals("DRAW", play(game, 0, 0, 0, 1, 0, 2, 1, 1, 1, 0, 1, 2, 2, 1, 2, 0, 2, 2));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("steps")
    void occupiedSquareIsInvalid(String step, Supplier<Game> game) {
        assertEquals("INVALID", play(game, 0, 0, 0, 0));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("steps")
    void squaresOffTheBoardAreInvalid(String step, Supplier<Game> game) {
        assertEquals("INVALID", play(game, -1, 0));
        assertEquals("INVALID", play(game, 0, 3));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("steps")
    void anInvalidMoveDoesNotCostTheTurn(String step, Supplier<Game> game) {
        // X(0,0), O tries the same square (invalid) and then plays again: O must still be O.
        assertEquals("O", play(game, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 2, 2, 1, 2));
    }
}

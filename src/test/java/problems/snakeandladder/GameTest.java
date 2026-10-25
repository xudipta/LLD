package problems.snakeandladder;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private static Dice scripted(int... rolls) {
        Iterator<Integer> it = java.util.Arrays.stream(rolls).boxed().iterator();
        return it::next;
    }

    private static final List<String> PLAYERS = List.of("A", "B");

    @Test
    void movesByTheRoll() {
        Game g = new Game(new Board(20), scripted(4), PLAYERS);
        Move m = g.takeTurn();
        assertEquals(new Move("A", 4, 0, 4), m);
    }

    @Test
    void playersAlternate() {
        Game g = new Game(new Board(20), scripted(1, 2, 3), PLAYERS);
        assertEquals("A", g.takeTurn().player());
        assertEquals("B", g.takeTurn().player());
        assertEquals("A", g.takeTurn().player());
    }

    @Test
    void ladderMovesTheTokenUp() {
        Board board = new Board(20).addLadder(3, 12);
        Game g = new Game(board, scripted(3), PLAYERS);
        assertEquals(12, g.takeTurn().to());
    }

    @Test
    void snakeMovesTheTokenDown() {
        Board board = new Board(20).addSnake(9, 2);
        Game g = new Game(board, scripted(9), PLAYERS);
        assertEquals(2, g.takeTurn().to());
    }

    @Test
    void overshootingTheEndKeepsTheTokenInPlace() {
        Game g = new Game(new Board(10), scripted(6, 1, 6), PLAYERS);
        g.takeTurn();            // A -> 6
        g.takeTurn();            // B -> 1
        Move m = g.takeTurn();   // A rolls 6, would reach 12
        assertEquals(6, m.to());
        assertNull(g.getWinner());
    }

    @Test
    void landingExactlyOnTheLastSquareWins() {
        Game g = new Game(new Board(10), scripted(5, 1, 5), PLAYERS);
        g.takeTurn(); g.takeTurn(); g.takeTurn();
        assertEquals("A", g.getWinner());
    }

    @Test
    void ladderToTheLastSquareWins() {
        Board board = new Board(10).addLadder(4, 10);
        Game g = new Game(board, scripted(4), PLAYERS);
        g.takeTurn();
        assertEquals("A", g.getWinner());
    }

    @Test
    void cannotPlayAfterTheGameEnds() {
        Game g = new Game(new Board(2), scripted(2), PLAYERS);
        g.takeTurn();
        assertThrows(IllegalStateException.class, g::takeTurn);
    }

    @Test
    void playRunsToACompletedGame() {
        Board board = new Board(100).addLadder(2, 38).addSnake(99, 10);
        Game g = new Game(board, new RandomDice(6, new Random(1)), PLAYERS);
        String winner = g.play();
        assertTrue(PLAYERS.contains(winner));
        assertEquals(100, g.positionOf(winner));
    }

    @Test
    void needsAtLeastTwoPlayers() {
        assertThrows(IllegalArgumentException.class, () -> new Game(new Board(10), scripted(1), List.of("A")));
    }

    // ---- board validation ----
    @Test
    void ladderMustGoUpAndSnakeMustGoDown() {
        assertThrows(IllegalArgumentException.class, () -> new Board(10).addLadder(5, 3));
        assertThrows(IllegalArgumentException.class, () -> new Board(10).addSnake(3, 5));
    }

    @Test
    void jumpsMayNotChainOrOverlap() {
        Board b = new Board(20).addLadder(3, 12);
        assertThrows(IllegalArgumentException.class, () -> b.addSnake(12, 5));   // starts where a ladder ends
        assertThrows(IllegalArgumentException.class, () -> b.addLadder(3, 15));  // same start twice
        assertThrows(IllegalArgumentException.class, () -> b.addLadder(2, 3));   // ends on a start
    }

    @Test
    void nothingMayStartOnTheLastSquare() {
        assertThrows(IllegalArgumentException.class, () -> new Board(10).addSnake(10, 2));
    }
}

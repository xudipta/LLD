package problems.tictactoe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import problems.tictactoe.models.Player;

import static org.junit.jupiter.api.Assertions.*;

class TicTacToeGameTest {
    TicTacToeGame game;
    Player p1;

    @BeforeEach
    void setUp() {
        game = new TicTacToeGame();
        game.initializeGame();
        p1 = game.players.peekFirst();
    }

    private void place(int r, int c) {
        assertTrue(game.board.addPiece(r, c, p1.getPiece()));
    }

    @Test
    void noWinnerOnEmptyBoard() {
        assertFalse(game.isWinner(0, 0, p1));
    }

    @Test
    void detectsRowWin() {
        place(1, 0); place(1, 1); place(1, 2);
        assertTrue(game.isWinner(1, 2, p1));
    }

    @Test
    void detectsColumnWin() {
        place(0, 2); place(1, 2); place(2, 2);
        assertTrue(game.isWinner(2, 2, p1));
    }

    @Test
    void detectsDiagonalWin() {
        place(0, 0); place(1, 1); place(2, 2);
        assertTrue(game.isWinner(2, 2, p1));
    }

    @Test
    void detectsAntiDiagonalWin() {
        place(0, 2); place(1, 1); place(2, 0);
        assertTrue(game.isWinner(2, 0, p1));
    }

    @Test
    void twoInARowIsNotAWin() {
        place(0, 0); place(0, 1);
        assertFalse(game.isWinner(0, 1, p1));
    }
}

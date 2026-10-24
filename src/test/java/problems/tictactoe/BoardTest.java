package problems.tictactoe;

import org.junit.jupiter.api.Test;
import problems.tictactoe.models.Board;
import problems.tictactoe.models.PieceX;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @Test
    void acceptsMoveOnEmptyCell() {
        Board board = new Board(3);
        assertTrue(board.addPiece(1, 1, new PieceX()));
    }

    @Test
    void rejectsMoveOnOccupiedCell() {
        Board board = new Board(3);
        board.addPiece(0, 0, new PieceX());
        assertFalse(board.addPiece(0, 0, new PieceX()));
    }

    @Test
    void rejectsMoveOutsideBoard() {
        Board board = new Board(3);
        assertFalse(board.addPiece(3, 0, new PieceX()));
        assertFalse(board.addPiece(0, 3, new PieceX()));
    }

    @Test
    void freeCellsShrinkAsPiecesArePlaced() {
        Board board = new Board(3);
        assertEquals(9, board.getFreeCells().size());
        board.addPiece(0, 0, new PieceX());
        assertEquals(8, board.getFreeCells().size());
    }
}

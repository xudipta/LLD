package problems.chess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private static Piece w(PieceType t) { return new Piece(t, Color.WHITE); }
    private static Piece b(PieceType t) { return new Piece(t, Color.BLACK); }

    private static Board board(Object... placements) {
        Board board = Board.empty();
        for (int i = 0; i < placements.length; i += 2) {
            board.place((Piece) placements[i], Position.parse((String) placements[i + 1]));
        }
        return board;
    }

    // ---- setup and basics ----
    @Test
    void initialPositionHasThirtyTwoPiecesAndWhiteMoves() {
        Game g = new Game();
        int count = 0;
        for (char f = 'a'; f <= 'h'; f++) for (char r = '1'; r <= '8'; r++) if (g.pieceAt("" + f + r) != null) count++;
        assertEquals(32, count);
        assertEquals(Color.WHITE, g.getTurn());
        assertEquals(PieceType.KING, g.pieceAt("e1").type());
        assertEquals(PieceType.QUEEN, g.pieceAt("d8").type());
        assertEquals(GameStatus.IN_PROGRESS, g.status());
    }

    @Test
    void parsesAndPrintsSquares() {
        assertEquals(new Position(1, 4), Position.parse("e2"));
        assertEquals("h8", new Position(7, 7).toString());
        assertThrows(IllegalArgumentException.class, () -> Position.parse("i9"));
        assertThrows(IllegalArgumentException.class, () -> Position.parse("e"));
    }

    @Test
    void turnsAlternate() {
        Game g = new Game();
        g.move("e2", "e4");
        assertEquals(Color.BLACK, g.getTurn());
        assertThrows(IllegalArgumentException.class, () -> g.move("d2", "d4"));   // White again
        g.move("e7", "e5");
        assertEquals(Color.WHITE, g.getTurn());
    }

    @Test
    void cannotMoveFromAnEmptySquare() {
        assertThrows(IllegalArgumentException.class, () -> new Game().move("e4", "e5"));
    }

    // ---- pawns ----
    @Test
    void pawnMovesOneOrTwoFromTheStartButOnlyOneAfter() {
        Game g = new Game();
        g.move("e2", "e4");
        g.move("a7", "a6");
        assertThrows(IllegalArgumentException.class, () -> g.move("e4", "e6"));
        g.move("e4", "e5");
    }

    @Test
    void pawnCannotMoveBackwardsOrSideways() {
        Game g = new Game(board(w(PieceType.KING), "e1", b(PieceType.KING), "e8", w(PieceType.PAWN), "d4"), Color.WHITE);
        assertThrows(IllegalArgumentException.class, () -> g.move("d4", "d3"));
        assertThrows(IllegalArgumentException.class, () -> g.move("d4", "e4"));
    }

    @Test
    void pawnCannotCaptureStraightAheadButCapturesDiagonally() {
        Game g = new Game(board(w(PieceType.KING), "e1", b(PieceType.KING), "e8",
                w(PieceType.PAWN), "d4", b(PieceType.PAWN), "d5", b(PieceType.KNIGHT), "e5"), Color.WHITE);
        assertThrows(IllegalArgumentException.class, () -> g.move("d4", "d5"));
        g.move("d4", "e5");
        assertEquals(PieceType.PAWN, g.pieceAt("e5").type());
        assertEquals(Color.WHITE, g.pieceAt("e5").color());
    }

    @Test
    void pawnCannotMoveDiagonallyToAnEmptySquare() {
        Game g = new Game(board(w(PieceType.KING), "e1", b(PieceType.KING), "e8", w(PieceType.PAWN), "d4"), Color.WHITE);
        assertThrows(IllegalArgumentException.class, () -> g.move("d4", "e5"));
    }

    @Test
    void pawnCannotJumpOverAPiece() {
        Game g = new Game();
        g.move("b1", "c3");
        g.move("e7", "e6");
        g.move("c3", "e4");
        g.move("e6", "e5");
        assertThrows(IllegalArgumentException.class, () -> g.move("e2", "e4"));   // blocked by the knight
    }

    @Test
    void pawnPromotesToAQueenOnTheLastRank() {
        Game g = new Game(board(w(PieceType.KING), "a1", b(PieceType.KING), "h5", w(PieceType.PAWN), "e7"), Color.WHITE);
        g.move("e7", "e8");
        assertEquals(PieceType.QUEEN, g.pieceAt("e8").type());
        assertEquals(Color.WHITE, g.pieceAt("e8").color());
    }

    // ---- other pieces ----
    @Test
    void knightJumpsOverPieces() {
        Game g = new Game();
        g.move("g1", "f3");
        assertEquals(PieceType.KNIGHT, g.pieceAt("f3").type());
        assertThrows(IllegalArgumentException.class, () -> new Game().move("g1", "g3"));
    }

    @Test
    void slidingPiecesCannotJumpOverPieces() {
        Game g = new Game();
        assertThrows(IllegalArgumentException.class, () -> g.move("a1", "a4"));   // rook behind its pawn
        assertThrows(IllegalArgumentException.class, () -> g.move("c1", "e3"));   // bishop behind its pawn
        assertThrows(IllegalArgumentException.class, () -> g.move("d1", "d3"));   // queen behind its pawn
    }

    @Test
    void rookBishopAndQueenMoveFreelyOnOpenLines() {
        Board bd = board(w(PieceType.KING), "e1", b(PieceType.KING), "e8",
                w(PieceType.ROOK), "a1", w(PieceType.BISHOP), "c1", w(PieceType.QUEEN), "d1");
        Game g = new Game(bd, Color.WHITE);
        g.move("a1", "a7");
        assertEquals(PieceType.ROOK, g.pieceAt("a7").type());
        g.move("e8", "f8");      // d8 is covered by the queen on the open d-file
        assertThrows(IllegalArgumentException.class, () -> new Game(bd, Color.BLACK).move("e8", "d8"));
        g.move("c1", "g5");
        assertEquals(PieceType.BISHOP, g.pieceAt("g5").type());
    }

    @Test
    void rookCannotMoveDiagonally() {
        Game g = new Game(board(w(PieceType.KING), "e1", b(PieceType.KING), "e8", w(PieceType.ROOK), "a1"), Color.WHITE);
        assertThrows(IllegalArgumentException.class, () -> g.move("a1", "b2"));
    }

    @Test
    void kingMovesOneSquareOnly() {
        Game g = new Game(board(w(PieceType.KING), "e4", b(PieceType.KING), "e8"), Color.WHITE);
        assertThrows(IllegalArgumentException.class, () -> g.move("e4", "e6"));
        g.move("e4", "e5");
    }

    @Test
    void cannotCaptureYourOwnPiece() {
        assertThrows(IllegalArgumentException.class, () -> new Game().move("a1", "a2"));
    }

    @Test
    void capturingRemovesThePiece() {
        Game g = new Game(board(w(PieceType.KING), "e1", b(PieceType.KING), "e8",
                w(PieceType.ROOK), "a1", b(PieceType.KNIGHT), "a6"), Color.WHITE);
        g.move("a1", "a6");
        assertEquals(PieceType.ROOK, g.pieceAt("a6").type());
        assertNull(g.pieceAt("a1"));
    }

    // ---- check, pins, mate ----
    @Test
    void aMoveThatLeavesYourKingInCheckIsIllegal() {
        // The white rook is pinned to its king by the black rook on the same file.
        Game g = new Game(board(w(PieceType.KING), "e1", w(PieceType.ROOK), "e2",
                b(PieceType.ROOK), "e8", b(PieceType.KING), "a8"), Color.WHITE);
        assertThrows(IllegalArgumentException.class, () -> g.move("e2", "d2"));
        g.move("e2", "e5");   // staying on the file keeps the king covered
    }

    @Test
    void kingCannotMoveIntoCheck() {
        Game g = new Game(board(w(PieceType.KING), "e1", b(PieceType.ROOK), "d8", b(PieceType.KING), "a8"), Color.WHITE);
        assertThrows(IllegalArgumentException.class, () -> g.move("e1", "d1"));
        g.move("e1", "e2");
    }

    @Test
    void checkIsReportedAndMustBeAnswered() {
        Game g = new Game(board(w(PieceType.KING), "e1", b(PieceType.ROOK), "e8", b(PieceType.KING), "a8"), Color.WHITE);
        assertEquals(GameStatus.CHECK, g.status());
        assertThrows(IllegalArgumentException.class, () -> g.move("e1", "e2"));   // still on the file
        g.move("e1", "d1");
        assertEquals(GameStatus.IN_PROGRESS, g.status());
    }

    @Test
    void pawnsAttackDiagonallyNotStraightAhead() {
        // A black pawn on e2 does not check a king on e1, but one on d2 does.
        Board straight = board(w(PieceType.KING), "e1", b(PieceType.PAWN), "e2", b(PieceType.KING), "a8");
        assertEquals(GameStatus.IN_PROGRESS, new Game(straight, Color.WHITE).status());
        Board diagonal = board(w(PieceType.KING), "e1", b(PieceType.PAWN), "d2", b(PieceType.KING), "a8");
        assertEquals(GameStatus.CHECK, new Game(diagonal, Color.WHITE).status());
    }

    @Test
    void foolsMateIsCheckmate() {
        Game g = new Game();
        g.move("f2", "f3");
        g.move("e7", "e5");
        g.move("g2", "g4");
        g.move("d8", "h4");
        assertEquals(GameStatus.CHECKMATE, g.status());
        assertTrue(g.isOver());
        assertThrows(IllegalStateException.class, () -> g.move("a2", "a3"));
    }

    @Test
    void stalemateIsDetectedWhenThereIsNoLegalMoveAndNoCheck() {
        Board bd = board(b(PieceType.KING), "h8", w(PieceType.KING), "f7", w(PieceType.QUEEN), "g6");
        Game g = new Game(bd, Color.BLACK);
        assertEquals(GameStatus.STALEMATE, g.status());
        assertTrue(g.isOver());
    }

    @Test
    void anEscapeMeansItIsNotMate() {
        Board bd = board(b(PieceType.KING), "h8", w(PieceType.KING), "a1", w(PieceType.ROOK), "a8");
        assertEquals(GameStatus.CHECK, new Game(bd, Color.BLACK).status());   // Kg7 or Kh7 escapes
    }
}

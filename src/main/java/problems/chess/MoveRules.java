package problems.chess;

/**
 * How each piece moves. Stateless and separate from Game, so the rules can be read, and tested, on their own.
 * Castling, en passant and under-promotion are not implemented.
 */
final class MoveRules {
    private MoveRules() {
    }

    /** True if the piece on {@code from} could move to {@code to} by its movement rules, ignoring check. */
    static boolean isPseudoLegal(Board board, Position from, Position to) {
        Piece piece = board.at(from);
        if (piece == null || from.equals(to)) {
            return false;
        }
        Piece target = board.at(to);
        if (target != null && target.color() == piece.color()) {
            return false;
        }
        int dr = to.row() - from.row();
        int dc = to.col() - from.col();
        int adr = Math.abs(dr);
        int adc = Math.abs(dc);
        return switch (piece.type()) {
            case KNIGHT -> adr * adc == 2;
            case KING -> Math.max(adr, adc) == 1;
            case ROOK -> (dr == 0 || dc == 0) && pathClear(board, from, to);
            case BISHOP -> adr == adc && pathClear(board, from, to);
            case QUEEN -> (dr == 0 || dc == 0 || adr == adc) && pathClear(board, from, to);
            case PAWN -> pawnMove(board, piece.color(), from, to, target, dr, adc);
        };
    }

    private static boolean pawnMove(Board board, Color color, Position from, Position to, Piece target, int dr, int adc) {
        int forward = color == Color.WHITE ? 1 : -1;
        int startRow = color == Color.WHITE ? 1 : 6;
        if (adc == 0) {
            if (target != null) return false;                       // pawns never capture straight ahead
            if (dr == forward) return true;
            return dr == 2 * forward && from.row() == startRow && board.at(from.row() + forward, from.col()) == null;
        }
        return adc == 1 && dr == forward && target != null;         // diagonal step only to capture
    }

    private static boolean pathClear(Board board, Position from, Position to) {
        int stepR = Integer.signum(to.row() - from.row());
        int stepC = Integer.signum(to.col() - from.col());
        int r = from.row() + stepR;
        int c = from.col() + stepC;
        while (r != to.row() || c != to.col()) {
            if (board.at(r, c) != null) return false;
            r += stepR;
            c += stepC;
        }
        return true;
    }

    /** Is the king of {@code color} attacked? */
    static boolean isInCheck(Board board, Color color) {
        Position king = board.findKing(color);
        if (king == null) return false;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board.at(r, c);
                if (p != null && p.color() != color && isPseudoLegal(board, new Position(r, c), king)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Fully legal: obeys the piece's rules and does not leave the mover's own king in check. */
    static boolean isLegal(Board board, Position from, Position to) {
        Piece piece = board.at(from);
        if (piece == null || !isPseudoLegal(board, from, to)) {
            return false;
        }
        Board after = board.copy();
        apply(after, from, to);
        return !isInCheck(after, piece.color());
    }

    /** Moves the piece; a pawn reaching the last rank becomes a queen. */
    static void apply(Board board, Position from, Position to) {
        Piece piece = board.at(from);
        board.move(from, to);
        boolean lastRank = to.row() == (piece.color() == Color.WHITE ? 7 : 0);
        if (piece.type() == PieceType.PAWN && lastRank) {
            board.place(new Piece(PieceType.QUEEN, piece.color()), to);
        }
    }

    static boolean hasLegalMove(Board board, Color color) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board.at(r, c);
                if (p == null || p.color() != color) continue;
                Position from = new Position(r, c);
                for (int tr = 0; tr < 8; tr++) {
                    for (int tc = 0; tc < 8; tc++) {
                        if (isLegal(board, from, new Position(tr, tc))) return true;
                    }
                }
            }
        }
        return false;
    }
}

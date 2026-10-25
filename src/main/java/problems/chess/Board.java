package problems.chess;

public class Board {
    private final Piece[][] squares = new Piece[8][8];

    public static Board empty() {
        return new Board();
    }

    public static Board initial() {
        Board b = new Board();
        PieceType[] back = {PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP, PieceType.QUEEN,
                PieceType.KING, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK};
        for (int col = 0; col < 8; col++) {
            b.squares[0][col] = new Piece(back[col], Color.WHITE);
            b.squares[1][col] = new Piece(PieceType.PAWN, Color.WHITE);
            b.squares[6][col] = new Piece(PieceType.PAWN, Color.BLACK);
            b.squares[7][col] = new Piece(back[col], Color.BLACK);
        }
        return b;
    }

    public Piece at(Position p) {
        return squares[p.row()][p.col()];
    }

    public Piece at(int row, int col) {
        return squares[row][col];
    }

    public void place(Piece piece, Position p) {
        squares[p.row()][p.col()] = piece;
    }

    void move(Position from, Position to) {
        squares[to.row()][to.col()] = squares[from.row()][from.col()];
        squares[from.row()][from.col()] = null;
    }

    public Board copy() {
        Board b = new Board();
        for (int r = 0; r < 8; r++) {
            b.squares[r] = squares[r].clone();   // Piece is immutable, so sharing pieces is safe
        }
        return b;
    }

    Position findKing(Color color) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = squares[r][c];
                if (p != null && p.type() == PieceType.KING && p.color() == color) {
                    return new Position(r, c);
                }
            }
        }
        return null;
    }
}

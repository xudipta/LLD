package problems.chess;

/** A square. {@code row} 0 is rank 1 (White's back rank) and {@code col} 0 is file a. */
public record Position(int row, int col) {
    public Position {
        if (row < 0 || row > 7 || col < 0 || col > 7) {
            throw new IllegalArgumentException("Off the board: " + row + "," + col);
        }
    }

    /** Parses algebraic notation such as "e2". */
    public static Position parse(String square) {
        if (square == null || square.length() != 2) {
            throw new IllegalArgumentException("Bad square: " + square);
        }
        return new Position(square.charAt(1) - '1', square.charAt(0) - 'a');
    }

    @Override
    public String toString() {
        return "" + (char) ('a' + col) + (char) ('1' + row);
    }
}

package problems.chess;

public class Game {
    private final Board board;
    private Color turn;

    public Game() {
        this(Board.initial(), Color.WHITE);
    }

    /** Starts from any position, e.g. to set up a test. */
    public Game(Board board, Color toMove) {
        this.board = board;
        this.turn = toMove;
    }

    public void move(String from, String to) {
        move(Position.parse(from), Position.parse(to));
    }

    public void move(Position from, Position to) {
        if (isOver()) {
            throw new IllegalStateException("The game is over: " + status());
        }
        Piece piece = board.at(from);
        if (piece == null) {
            throw new IllegalArgumentException("No piece on " + from);
        }
        if (piece.color() != turn) {
            throw new IllegalArgumentException("It is " + turn + "'s turn");
        }
        if (!MoveRules.isLegal(board, from, to)) {
            throw new IllegalArgumentException("Illegal move: " + from + " to " + to);
        }
        MoveRules.apply(board, from, to);
        turn = turn.opposite();
    }

    /** Status for the side to move. */
    public GameStatus status() {
        boolean inCheck = MoveRules.isInCheck(board, turn);
        boolean canMove = MoveRules.hasLegalMove(board, turn);
        if (!canMove) {
            return inCheck ? GameStatus.CHECKMATE : GameStatus.STALEMATE;
        }
        return inCheck ? GameStatus.CHECK : GameStatus.IN_PROGRESS;
    }

    public boolean isOver() {
        GameStatus s = status();
        return s == GameStatus.CHECKMATE || s == GameStatus.STALEMATE;
    }

    public Color getTurn() { return turn; }

    public Piece pieceAt(String square) { return board.at(Position.parse(square)); }
}

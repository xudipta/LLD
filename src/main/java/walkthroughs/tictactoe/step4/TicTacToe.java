package walkthroughs.tictactoe.step4;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/** STEP 4 - pure rules. No Scanner, no println: anything can drive it (console, tests, a web handler). */
public class TicTacToe {
    private final Board board;
    private final Deque<Player> turnOrder;
    private int moves = 0;

    public TicTacToe(int size, List<Player> players) {
        this.board = new Board(size);
        this.turnOrder = new ArrayDeque<>(players);
    }

    public MoveResult move(int r, int c) {
        Player p = turnOrder.peekFirst();
        if (!board.place(r, c, p)) return new MoveResult(MoveResult.Outcome.INVALID, null);
        moves++;
        if (winsThrough(r, c, p)) return new MoveResult(MoveResult.Outcome.WIN, p);
        if (moves == board.size() * board.size()) return new MoveResult(MoveResult.Outcome.DRAW, null);
        turnOrder.addLast(turnOrder.removeFirst());
        return new MoveResult(MoveResult.Outcome.CONTINUE, null);
    }

    public Player currentPlayer() { return turnOrder.peekFirst(); }

    public Board board() { return board; }

    private boolean winsThrough(int r, int c, Player p) {
        int n = board.size();
        boolean row = true, col = true, diag = r == c, anti = r + c == n - 1;
        for (int i = 0; i < n; i++) {
            row &= board.at(r, i) == p;
            col &= board.at(i, c) == p;
            if (diag) diag = board.at(i, i) == p;
            if (anti) anti = board.at(i, n - 1 - i) == p;
        }
        return row || col || diag || anti;
    }
}

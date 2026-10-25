package walkthroughs.tictactoe.step3;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

/**
 * STEP 3 - players are objects, turn order is a queue, and winning is checked only through the
 * square just played. But the console loop still lives inside the game (see step 4).
 */
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
        if (board.isFull(moves)) return new MoveResult(MoveResult.Outcome.DRAW, null);
        turnOrder.addLast(turnOrder.removeFirst());
        return new MoveResult(MoveResult.Outcome.CONTINUE, null);
    }

    /** O(n) per move instead of rescanning the whole board. */
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

    /** Smell: input parsing, prompts and game rules all live in one class. */
    public String playInteractively() {
        Scanner in = new Scanner(System.in);
        while (true) {
            Player p = turnOrder.peekFirst();
            System.out.println(p.name() + ", enter row and column:");
            String[] parts = in.nextLine().trim().split("[,\\s]+");
            MoveResult result = move(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            switch (result.outcome()) {
                case WIN -> { return result.winner().name(); }
                case DRAW -> { return "tie"; }
                case INVALID -> System.out.println("Illegal move, try again");
                default -> { }
            }
        }
    }
}

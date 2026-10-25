package walkthroughs.tictactoe.step2;

public class TicTacToe {
    private final Board board;
    private char turn = 'X';
    private int moves = 0;

    public TicTacToe(int size) {
        this.board = new Board(size);
    }

    public Outcome move(int r, int c) {
        if (!board.place(r, c, turn)) return Outcome.INVALID;
        moves++;
        if (hasWon(turn)) return turn == 'X' ? Outcome.X_WINS : Outcome.O_WINS;
        if (moves == board.size() * board.size()) return Outcome.DRAW;
        turn = turn == 'X' ? 'O' : 'X';
        return Outcome.CONTINUE;
    }

    /** One loop per kind of line replaces eight copy-pasted conditions, and works for any size. */
    private boolean hasWon(char s) {
        int n = board.size();
        for (int i = 0; i < n; i++) {
            boolean row = true, col = true;
            for (int j = 0; j < n; j++) {
                row &= board.at(i, j) == s;
                col &= board.at(j, i) == s;
            }
            if (row || col) return true;
        }
        boolean diag = true, anti = true;
        for (int i = 0; i < n; i++) {
            diag &= board.at(i, i) == s;
            anti &= board.at(i, n - 1 - i) == s;
        }
        return diag || anti;
    }

    public String render() { return board.render(); }
}

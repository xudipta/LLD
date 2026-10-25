package walkthroughs.tictactoe.step1;

/**
 * STEP 1 - the first thing that works. Deliberately naive; see docs/walkthroughs/tictactoe.md for the smells.
 */
public class TicTacToe {
    private final char[][] b = new char[3][3];
    private char turn = 'X';
    private int moves = 0;

    public String move(int r, int c) {
        if (r < 0 || r > 2 || c < 0 || c > 2) return "INVALID";
        if (b[r][c] != 0) return "INVALID";
        b[r][c] = turn;
        moves++;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(b[i][j] == 0 ? " . " : " " + b[i][j] + " ");
            }
            System.out.println();
        }
        if (b[0][0] == turn && b[0][1] == turn && b[0][2] == turn) return String.valueOf(turn);
        if (b[1][0] == turn && b[1][1] == turn && b[1][2] == turn) return String.valueOf(turn);
        if (b[2][0] == turn && b[2][1] == turn && b[2][2] == turn) return String.valueOf(turn);
        if (b[0][0] == turn && b[1][0] == turn && b[2][0] == turn) return String.valueOf(turn);
        if (b[0][1] == turn && b[1][1] == turn && b[2][1] == turn) return String.valueOf(turn);
        if (b[0][2] == turn && b[1][2] == turn && b[2][2] == turn) return String.valueOf(turn);
        if (b[0][0] == turn && b[1][1] == turn && b[2][2] == turn) return String.valueOf(turn);
        if (b[0][2] == turn && b[1][1] == turn && b[2][0] == turn) return String.valueOf(turn);
        if (moves == 9) return "DRAW";
        if (turn == 'X') turn = 'O'; else turn = 'X';
        return "CONTINUE";
    }
}

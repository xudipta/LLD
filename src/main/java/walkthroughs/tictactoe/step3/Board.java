package walkthroughs.tictactoe.step3;

public class Board {
    private final int size;
    private final Player[][] cells;

    public Board(int size) {
        this.size = size;
        this.cells = new Player[size][size];
    }

    public int size() { return size; }

    public Player at(int r, int c) { return cells[r][c]; }

    public boolean place(int r, int c, Player p) {
        if (r < 0 || r >= size || c < 0 || c >= size || cells[r][c] != null) return false;
        cells[r][c] = p;
        return true;
    }

    public boolean isFull(int moves) { return moves == size * size; }
}

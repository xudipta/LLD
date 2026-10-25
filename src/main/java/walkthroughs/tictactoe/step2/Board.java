package walkthroughs.tictactoe.step2;

/** STEP 2 - the grid and its rules get their own class, and the size is a parameter. */
public class Board {
    private final int size;
    private final char[][] cells;

    public Board(int size) {
        this.size = size;
        this.cells = new char[size][size];
    }

    public int size() { return size; }

    public char at(int r, int c) { return cells[r][c]; }

    public boolean place(int r, int c, char symbol) {
        if (r < 0 || r >= size || c < 0 || c >= size || cells[r][c] != 0) return false;
        cells[r][c] = symbol;
        return true;
    }

    /** Rendering returns text instead of printing, so callers decide where it goes. */
    public String render() {
        StringBuilder sb = new StringBuilder();
        for (char[] row : cells) {
            for (char cell : row) sb.append(cell == 0 ? " . " : " " + cell + " ");
            sb.append('\n');
        }
        return sb.toString();
    }
}

package walkthroughs.tictactoe.step4;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/** All the I/O, with the streams injected so it can be tested with scripted input. */
public class ConsoleRunner {
    private final TicTacToe game;
    private final Scanner in;
    private final PrintStream out;

    public ConsoleRunner(TicTacToe game, InputStream in, PrintStream out) {
        this.game = game;
        this.in = new Scanner(in);
        this.out = out;
    }

    /** @return the winner's name, or "tie" */
    public String run() {
        while (true) {
            Player p = game.currentPlayer();
            out.println(p.name() + ", enter row and column:");
            int[] rc = parse(in.nextLine());
            if (rc == null) {
                out.println("Please enter two numbers, e.g. 1 2");
                continue;
            }
            MoveResult result = game.move(rc[0], rc[1]);
            switch (result.outcome()) {
                case INVALID -> out.println("Illegal move, try again");
                case WIN -> { out.print(game.board().render()); return result.winner().name(); }
                case DRAW -> { out.print(game.board().render()); return "tie"; }
                case CONTINUE -> out.print(game.board().render());
            }
        }
    }

    /** Accepts "1 2" or "1,2"; returns null for anything else instead of crashing. */
    static int[] parse(String line) {
        String[] parts = line.trim().split("[,\\s]+");
        if (parts.length != 2) return null;
        try {
            return new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

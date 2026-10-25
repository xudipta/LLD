package problems.snakeandladder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private final Board board;
    private final Dice dice;
    private final List<String> order;
    private final Map<String, Integer> positions = new LinkedHashMap<>();
    private int turn;
    private String winner;

    public Game(Board board, Dice dice, List<String> players) {
        if (players.size() < 2) {
            throw new IllegalArgumentException("At least two players are needed");
        }
        this.board = board;
        this.dice = dice;
        this.order = new ArrayList<>(players);
        players.forEach(p -> positions.put(p, 0));   // 0 = not yet on the board
    }

    /** Plays one turn. Overshooting the last square leaves the token where it was. */
    public Move takeTurn() {
        if (winner != null) {
            throw new IllegalStateException("Game is over: " + winner + " has won");
        }
        String player = order.get(turn);
        int roll = dice.roll();
        int from = positions.get(player);
        int landing = from + roll;
        int to = landing > board.getSize() ? from : board.resolve(landing);
        positions.put(player, to);
        if (to == board.getSize()) {
            winner = player;
        }
        turn = (turn + 1) % order.size();
        return new Move(player, roll, from, to);
    }

    /** Plays to the end and returns the winner. */
    public String play() {
        while (winner == null) {
            takeTurn();
        }
        return winner;
    }

    public String getWinner() { return winner; }

    public int positionOf(String player) { return positions.get(player); }
}

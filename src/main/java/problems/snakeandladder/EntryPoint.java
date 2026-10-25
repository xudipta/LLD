package problems.snakeandladder;

import java.util.List;
import java.util.Random;

public class EntryPoint {
    public static void main(String[] args) {
        Board board = new Board(100)
                .addLadder(2, 38).addLadder(7, 14).addLadder(28, 84)
                .addSnake(16, 6).addSnake(46, 25).addSnake(99, 10);
        Game game = new Game(board, new RandomDice(6, new Random(42)), List.of("Asha", "Ben"));
        System.out.println("Winner: " + game.play());
    }
}

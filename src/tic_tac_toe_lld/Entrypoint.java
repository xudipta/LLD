package tic_tac_toe_lld;

public class Entrypoint {
    public static void main(String[] args) {
        TicTacToeGame game = new TicTacToeGame();

        game.initializeGame();
        System.out.println("Winner : "+game.startGame());
    }
}

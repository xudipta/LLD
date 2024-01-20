package tic_tac_toe_lld;

import java.util.Scanner;

public class Entrypoint {
    public static void main(String[] args) {
        TicTacToeGame game = new TicTacToeGame();

        game.initializeGame();

        boolean continuePlay=true;
        while(continuePlay){
            System.out.println("Winner : "+game.startGame());
            System.out.println("Do you want play again(y/n): ");
            Scanner sc = new Scanner(System.in);
            String in = sc.nextLine();
            if(!in.equalsIgnoreCase("y")) continuePlay =false;
        }
    }
}

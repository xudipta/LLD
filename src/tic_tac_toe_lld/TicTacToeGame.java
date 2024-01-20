package tic_tac_toe_lld;

import tic_tac_toe_lld.models.Board;
import tic_tac_toe_lld.models.PieceO;
import tic_tac_toe_lld.models.PieceX;
import tic_tac_toe_lld.models.Player;

import java.awt.*;
import java.util.*;
import java.util.List;

public class TicTacToeGame {
    Deque<Player> players;
    Board board;

    public void initializeGame(){
        players= new LinkedList<>();

        Player player1 = new Player("player1",new PieceO());
        Player player2 = new Player("player2",new PieceX());
        players.add(player1);
        players.add(player2);
        board=new Board(3);
        board.printBoard();
    }
    public String startGame(){
        boolean noWinner=true;
        while (noWinner){
            Player p=players.removeFirst();
            List<Point> freeCells=board.getFreeCells();
            if(freeCells.isEmpty()){
                noWinner=false;
                continue;
            }
            System.out.println(p.getName()+" ,Enter row , column: ");
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();
            if(input == null || input.isEmpty()) {
                System.out.println("Illegal move, please try again ");
                players.addFirst(p);
                continue;
            }
            input = input.replace(',',' ');
            String[] rc=input.split("\\s+");
            int r = Integer.parseInt(rc[0]);
            int c = Integer.parseInt(rc[1]);



            boolean isLegalMove = board.addPiece(r,c,p.getPiece());
            if(isLegalMove){
                board.printBoard();
                boolean isWinner = isWinner(r,c,p);
                if(isWinner){
                    return p.getName();
                }
                players.addLast(p);
            }else{
                players.addFirst(p);
                System.out.println("Illegal move, please try again");
            }

        }
        return "tie";
    }
    public boolean isWinner(int row, int col, Player p){
        boolean rowMatching=true;
        boolean colMatching=true;
        boolean diagonalMatching=true;
        boolean antiDiagonalMatching=true;
        //check for row
        for(int i=0;i<board.getBoardSize();i++){
            if(board.getBoard()[row][i]==null || board.getBoard()[row][i]!=p.getPiece()){
                rowMatching=false;
            }
        }
        //check for column
        for(int i=0;i<board.getBoardSize();i++){
            if(board.getBoard()[i][col]==null || board.getBoard()[i][col]!=p.getPiece()){
                colMatching=false;
            }
        }
        //check for diagonal
        for(int i=0;i<board.getBoardSize();i++){
            if(board.getBoard()[i][i]==null || board.getBoard()[i][i]!=p.getPiece()){
                diagonalMatching=false;
            }
        }
        //check for anti diagonal
        for(int i=0;i<board.getBoardSize();i++){
            if(board.getBoard()[i][board.getBoardSize()-1-i]==null || board.getBoard()[i][board.getBoardSize()-1-i]!=p.getPiece()){
                antiDiagonalMatching=false;
            }
        }

        return rowMatching || colMatching || diagonalMatching || antiDiagonalMatching;
    }


}

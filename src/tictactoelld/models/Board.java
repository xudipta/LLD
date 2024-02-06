package tictactoelld.models;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Board {
    int size;
    PlayingPiece[][] board;
    public Board(int s){
        this.size=s;
        board= new PlayingPiece[size][size];
    }

    public boolean addPiece(int r, int c, PlayingPiece playingPiece){
        if(r>size-1|| c>size-1) return false;
        if(board[r][c]==null){
            board[r][c]=playingPiece;
            return true;
        }
        return false;
    }

    public int getBoardSize(){
        return size;
    }
    public PlayingPiece[][] getBoard(){
        return board;
    }
    public void printBoard(){
        for(int i =0; i<size;i++){
            for(int j=0; j<size;j++){
                if(board[i][j] == null){
                    System.out.print("   "+"|");
                }
                else{
                    System.out.print(" "+board[i][j].type.name()+" |");
                }
            }
            System.out.println();

        }
    }
    public List<Point> getFreeCells(){
        List<Point> list= new ArrayList<>();
        for(int i =0; i<size;i++){
            for(int j=0; j<size;j++) {
                if(board[i][j]==null){
                    list.add(new Point(i,j));
                }
            }
        }
        return list;
    }
}

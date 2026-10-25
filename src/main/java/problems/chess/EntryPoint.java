package problems.chess;

public class EntryPoint {
    public static void main(String[] args) {
        Game game = new Game();
        // Fool's mate
        game.move("f2", "f3");
        game.move("e7", "e5");
        game.move("g2", "g4");
        game.move("d8", "h4");
        System.out.println("Status: " + game.status() + " (" + game.getTurn() + " to move)");
    }
}

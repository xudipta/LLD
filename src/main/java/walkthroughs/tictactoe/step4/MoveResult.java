package walkthroughs.tictactoe.step4;

public record MoveResult(Outcome outcome, Player winner) {
    public enum Outcome { INVALID, CONTINUE, WIN, DRAW }
}

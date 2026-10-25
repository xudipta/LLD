package walkthroughs.tictactoe.step3;

/** What a move did. {@code winner} is set only when {@code outcome} is WIN. */
public record MoveResult(Outcome outcome, Player winner) {
    public enum Outcome { INVALID, CONTINUE, WIN, DRAW }
}

package problems.elevator;

/** What happened in one tick: where the car is, which way it is heading and whether the doors opened. */
public record StepResult(int floor, Direction direction, boolean stopped) {
}

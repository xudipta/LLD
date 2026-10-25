package problems.bookmyshow;

public class SeatUnavailableException extends RuntimeException {
    public SeatUnavailableException(String seat) {
        super("Seat not available: " + seat);
    }
}

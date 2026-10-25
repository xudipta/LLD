package problems.bookmyshow;

import java.time.Instant;
import java.util.Set;

public class Booking {
    private final String id;
    private final String showId;
    private final String userId;
    private final Set<String> seats;
    private final Instant heldUntil;
    private BookingState state = BookingState.HELD;

    Booking(String id, String showId, String userId, Set<String> seats, Instant heldUntil) {
        this.id = id;
        this.showId = showId;
        this.userId = userId;
        this.seats = Set.copyOf(seats);
        this.heldUntil = heldUntil;
    }

    public String getId() { return id; }
    public String getShowId() { return showId; }
    public String getUserId() { return userId; }
    public Set<String> getSeats() { return seats; }
    public Instant getHeldUntil() { return heldUntil; }
    public BookingState getState() { return state; }

    void setState(BookingState state) { this.state = state; }
}

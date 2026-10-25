package problems.bookmyshow;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Two-step booking: hold seats for a limited time, then confirm (payment would happen between
 * the two). Unconfirmed holds expire lazily the next time anyone looks at the show.
 */
public class BookingService {
    private final ConcurrentHashMap<String, Show> shows = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Booking> bookings = new ConcurrentHashMap<>();
    private final AtomicLong ids = new AtomicLong();
    private final Clock clock;
    private final Duration holdFor;

    public BookingService(Clock clock, Duration holdFor) {
        this.clock = clock;
        this.holdFor = holdFor;
    }

    public void addShow(String showId, String movie, Set<String> seats) {
        if (shows.putIfAbsent(showId, new Show(showId, movie, seats)) != null) {
            throw new IllegalArgumentException("Show already exists: " + showId);
        }
    }

    /** @throws SeatUnavailableException if any requested seat is taken (nothing is held then) */
    public Booking hold(String showId, String userId, Set<String> seats) {
        if (seats.isEmpty()) {
            throw new IllegalArgumentException("Choose at least one seat");
        }
        Show show = show(showId);
        Instant now = Instant.now(clock);
        Booking booking = new Booking("B-" + ids.incrementAndGet(), showId, userId, seats, now.plus(holdFor));
        show.hold(booking, now);
        bookings.put(booking.getId(), booking);
        return booking;
    }

    public void confirm(String bookingId) {
        Booking b = booking(bookingId);
        show(b.getShowId()).confirm(b, Instant.now(clock));
    }

    public void cancel(String bookingId) {
        Booking b = booking(bookingId);
        show(b.getShowId()).cancel(b);
    }

    public Set<String> availableSeats(String showId) {
        return show(showId).availableSeats(Instant.now(clock));
    }

    private Show show(String id) {
        Show s = shows.get(id);
        if (s == null) throw new IllegalArgumentException("Unknown show: " + id);
        return s;
    }

    private Booking booking(String id) {
        Booking b = bookings.get(id);
        if (b == null) throw new IllegalArgumentException("Unknown booking: " + id);
        return b;
    }
}

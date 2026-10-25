package problems.bookmyshow;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * One screening and its seats. Every operation is synchronized on the show, so a seat
 * can be claimed by only one booking even when many users try at once.
 * Different shows never contend with each other.
 */
class Show {
    private final String id;
    private final String movie;
    private final Set<String> allSeats;
    /** seat -> booking currently holding or owning it (held or confirmed). */
    private final Map<String, Booking> taken = new HashMap<>();

    Show(String id, String movie, Set<String> seats) {
        this.id = id;
        this.movie = movie;
        this.allSeats = Set.copyOf(seats);
    }

    String getId() { return id; }

    String getMovie() { return movie; }

    /** All-or-nothing: either every requested seat is claimed or none is. */
    synchronized void hold(Booking booking, Instant now) {
        for (String seat : booking.getSeats()) {
            if (!allSeats.contains(seat)) {
                throw new IllegalArgumentException("No such seat: " + seat);
            }
        }
        releaseExpired(now);
        for (String seat : booking.getSeats()) {
            if (taken.containsKey(seat)) {
                throw new SeatUnavailableException(seat);
            }
        }
        booking.getSeats().forEach(seat -> taken.put(seat, booking));
    }

    synchronized void confirm(Booking booking, Instant now) {
        releaseExpired(now);
        if (booking.getState() == BookingState.EXPIRED) {
            throw new IllegalStateException("Booking " + booking.getId() + " has expired");
        }
        if (booking.getState() != BookingState.HELD) {
            throw new IllegalStateException("Booking " + booking.getId() + " is " + booking.getState());
        }
        booking.setState(BookingState.CONFIRMED);
    }

    synchronized void cancel(Booking booking) {
        if (booking.getState() == BookingState.CANCELLED || booking.getState() == BookingState.EXPIRED) {
            throw new IllegalStateException("Booking " + booking.getId() + " is " + booking.getState());
        }
        booking.setState(BookingState.CANCELLED);
        booking.getSeats().forEach(seat -> taken.remove(seat, booking));
    }

    synchronized Set<String> availableSeats(Instant now) {
        releaseExpired(now);
        Set<String> free = new TreeSet<>(allSeats);
        free.removeAll(taken.keySet());
        return free;
    }

    /** Held bookings past their deadline lose their seats. Caller holds the lock. */
    private void releaseExpired(Instant now) {
        taken.values().stream().distinct().toList().forEach(b -> {
            if (b.getState() == BookingState.HELD && !now.isBefore(b.getHeldUntil())) {
                b.setState(BookingState.EXPIRED);
                b.getSeats().forEach(seat -> taken.remove(seat, b));
            }
        });
    }
}

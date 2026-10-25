package problems.bookmyshow;

import java.time.Clock;
import java.time.Duration;
import java.util.Set;

public class EntryPoint {
    public static void main(String[] args) {
        BookingService service = new BookingService(Clock.systemUTC(), Duration.ofMinutes(5));
        service.addShow("S1", "Dune", Set.of("A1", "A2", "A3"));
        Booking booking = service.hold("S1", "asha", Set.of("A1", "A2"));
        service.confirm(booking.getId());
        System.out.println("Available: " + service.availableSeats("S1"));
        try {
            service.hold("S1", "ben", Set.of("A2", "A3"));
        } catch (SeatUnavailableException e) {
            System.out.println(e.getMessage() + " (A3 stays free: " + service.availableSeats("S1") + ")");
        }
    }
}

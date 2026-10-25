package problems.bookmyshow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class BookingServiceTest {
    private static class MutableClock extends Clock {
        private volatile Instant now = Instant.parse("2026-01-01T10:00:00Z");

        void advance(Duration d) { now = now.plus(d); }

        @Override public ZoneId getZone() { return ZoneOffset.UTC; }
        @Override public Clock withZone(ZoneId zone) { return this; }
        @Override public Instant instant() { return now; }
    }

    private MutableClock clock;
    private BookingService service;

    @BeforeEach
    void setUp() {
        clock = new MutableClock();
        service = new BookingService(clock, Duration.ofMinutes(5));
        service.addShow("S1", "Dune", Set.of("A1", "A2", "A3"));
    }

    @Test
    void holdingSeatsRemovesThemFromAvailability() {
        service.hold("S1", "u1", Set.of("A1", "A2"));
        assertEquals(Set.of("A3"), service.availableSeats("S1"));
    }

    @Test
    void aSeatCannotBeHeldTwice() {
        service.hold("S1", "u1", Set.of("A1"));
        assertThrows(SeatUnavailableException.class, () -> service.hold("S1", "u2", Set.of("A1")));
    }

    @Test
    void aFailedBookingHoldsNoSeatsAtAll() {
        service.hold("S1", "u1", Set.of("A2"));
        assertThrows(SeatUnavailableException.class, () -> service.hold("S1", "u2", Set.of("A1", "A2")));
        assertEquals(Set.of("A1", "A3"), service.availableSeats("S1"));   // A1 was not left held
    }

    @Test
    void confirmedBookingsKeepTheirSeatsForever() {
        Booking b = service.hold("S1", "u1", Set.of("A1"));
        service.confirm(b.getId());
        clock.advance(Duration.ofDays(1));
        assertEquals(BookingState.CONFIRMED, b.getState());
        assertFalse(service.availableSeats("S1").contains("A1"));
    }

    @Test
    void unconfirmedHoldsExpireAndFreeTheSeats() {
        Booking b = service.hold("S1", "u1", Set.of("A1"));
        clock.advance(Duration.ofMinutes(5));
        assertTrue(service.availableSeats("S1").contains("A1"));
        assertEquals(BookingState.EXPIRED, b.getState());
        assertDoesNotThrow(() -> service.hold("S1", "u2", Set.of("A1")));
    }

    @Test
    void anExpiredBookingCannotBeConfirmed() {
        Booking b = service.hold("S1", "u1", Set.of("A1"));
        clock.advance(Duration.ofMinutes(6));
        assertThrows(IllegalStateException.class, () -> service.confirm(b.getId()));
    }

    @Test
    void confirmingBeforeTheDeadlineWorks() {
        Booking b = service.hold("S1", "u1", Set.of("A1"));
        clock.advance(Duration.ofMinutes(4));
        service.confirm(b.getId());
        assertEquals(BookingState.CONFIRMED, b.getState());
    }

    @Test
    void cancellingFreesTheSeats() {
        Booking b = service.hold("S1", "u1", Set.of("A1", "A2"));
        service.cancel(b.getId());
        assertEquals(Set.of("A1", "A2", "A3"), service.availableSeats("S1"));
        assertThrows(IllegalStateException.class, () -> service.cancel(b.getId()));
    }

    @Test
    void aConfirmedBookingCanBeCancelled() {
        Booking b = service.hold("S1", "u1", Set.of("A1"));
        service.confirm(b.getId());
        service.cancel(b.getId());
        assertTrue(service.availableSeats("S1").contains("A1"));
    }

    @Test
    void validatesInput() {
        assertThrows(IllegalArgumentException.class, () -> service.hold("S1", "u1", Set.of("Z9")));
        assertThrows(IllegalArgumentException.class, () -> service.hold("S1", "u1", Set.of()));
        assertThrows(IllegalArgumentException.class, () -> service.hold("NOPE", "u1", Set.of("A1")));
        assertThrows(IllegalArgumentException.class, () -> service.confirm("B-999"));
        assertThrows(IllegalArgumentException.class, () -> service.addShow("S1", "Again", Set.of("A1")));
    }

    @Test
    void showsAreIndependent() {
        service.addShow("S2", "Dune", Set.of("A1"));
        service.hold("S1", "u1", Set.of("A1"));
        assertDoesNotThrow(() -> service.hold("S2", "u2", Set.of("A1")));
    }

    @Test
    void exactlyOneOfManyConcurrentUsersGetsTheSeat() throws Exception {
        int users = 64;
        ExecutorService pool = Executors.newFixedThreadPool(users);
        CountDownLatch ready = new CountDownLatch(users);
        CountDownLatch go = new CountDownLatch(1);
        AtomicInteger winners = new AtomicInteger();
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < users; i++) {
            final String user = "u" + i;
            futures.add(pool.submit(() -> {
                ready.countDown();
                go.await();
                try {
                    service.hold("S1", user, Set.of("A1"));
                    winners.incrementAndGet();
                } catch (SeatUnavailableException expected) {
                    // lost the race
                }
                return null;
            }));
        }
        ready.await();
        go.countDown();
        for (Future<?> f : futures) f.get(30, TimeUnit.SECONDS);
        pool.shutdown();
        assertEquals(1, winners.get());
    }

    @Test
    void overlappingConcurrentBookingsNeverShareASeat() throws Exception {
        service.addShow("BIG", "Dune", Set.of("1", "2", "3", "4", "5", "6"));
        int users = 48;
        ExecutorService pool = Executors.newFixedThreadPool(users);
        CountDownLatch go = new CountDownLatch(1);
        List<Booking> won = Collections.synchronizedList(new ArrayList<>());
        List<Future<?>> futures = new ArrayList<>();
        List<Set<String>> requests = List.of(Set.of("1", "2"), Set.of("2", "3"), Set.of("3", "4"), Set.of("5", "6"), Set.of("6", "1"));
        for (int i = 0; i < users; i++) {
            final int n = i;
            futures.add(pool.submit(() -> {
                go.await();
                try {
                    won.add(service.hold("BIG", "u" + n, requests.get(n % requests.size())));
                } catch (SeatUnavailableException expected) {
                    // fine
                }
                return null;
            }));
        }
        go.countDown();
        for (Future<?> f : futures) f.get(30, TimeUnit.SECONDS);
        pool.shutdown();
        Set<String> seen = new HashSet<>();
        for (Booking b : won) {
            for (String seat : b.getSeats()) {
                assertTrue(seen.add(seat), "seat given twice: " + seat);
            }
        }
        assertFalse(won.isEmpty());
    }
}

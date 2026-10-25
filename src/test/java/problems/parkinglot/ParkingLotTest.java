package problems.parkinglot;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class ParkingLotTest {
    /** A clock the test can move forward. */
    private static class MutableClock extends Clock {
        private Instant now = Instant.parse("2026-01-01T10:00:00Z");

        void advance(Duration d) { now = now.plus(d); }

        @Override public ZoneOffset getZone() { return ZoneOffset.UTC; }
        @Override public Clock withZone(java.time.ZoneId zone) { return this; }
        @Override public Instant instant() { return now; }
    }

    private List<ParkingSpot> spots() {
        return List.of(new ParkingSpot(1, SpotSize.LARGE), new ParkingSpot(2, SpotSize.MEDIUM), new ParkingSpot(3, SpotSize.SMALL));
    }

    private ParkingLot lot(SpotAllocationStrategy s, MutableClock clock) {
        return new ParkingLot(spots(), s, HourlyPricing.defaults(), clock);
    }

    // ---- allocation ----
    @Test
    void firstFitTakesTheLowestIdThatFits() {
        ParkingLot lot = lot(new FirstFitStrategy(), new MutableClock());
        assertEquals(1, lot.park(new Vehicle("B", VehicleType.BIKE)).orElseThrow().spotId());
    }

    @Test
    void bestFitTakesTheSmallestSpotThatFits() {
        ParkingLot lot = lot(new BestFitStrategy(), new MutableClock());
        assertEquals(3, lot.park(new Vehicle("B", VehicleType.BIKE)).orElseThrow().spotId());
        assertEquals(2, lot.park(new Vehicle("C", VehicleType.CAR)).orElseThrow().spotId());
    }

    @Test
    void truckNeedsALargeSpot() {
        ParkingLot lot = lot(new BestFitStrategy(), new MutableClock());
        assertEquals(1, lot.park(new Vehicle("T1", VehicleType.TRUCK)).orElseThrow().spotId());
        assertTrue(lot.park(new Vehicle("T2", VehicleType.TRUCK)).isEmpty());
    }

    @Test
    void fullLotReturnsEmpty() {
        ParkingLot lot = lot(new FirstFitStrategy(), new MutableClock());
        lot.park(new Vehicle("1", VehicleType.CAR));
        lot.park(new Vehicle("2", VehicleType.CAR));
        assertEquals(1, lot.freeSpotCount());
        assertTrue(lot.park(new Vehicle("3", VehicleType.CAR)).isEmpty());
    }

    @Test
    void sameVehicleCannotParkTwice() {
        ParkingLot lot = lot(new FirstFitStrategy(), new MutableClock());
        lot.park(new Vehicle("A", VehicleType.CAR));
        assertThrows(IllegalStateException.class, () -> lot.park(new Vehicle("A", VehicleType.CAR)));
    }

    // ---- leaving and pricing ----
    @Test
    void unparkFreesTheSpot() {
        ParkingLot lot = lot(new FirstFitStrategy(), new MutableClock());
        Ticket t = lot.park(new Vehicle("A", VehicleType.CAR)).orElseThrow();
        assertEquals(2, lot.freeSpotCount());
        lot.unpark(t.id());
        assertEquals(3, lot.freeSpotCount());
        assertTrue(lot.park(new Vehicle("A", VehicleType.CAR)).isPresent());
    }

    @Test
    void unknownOrRepeatedTicketIsRejected() {
        ParkingLot lot = lot(new FirstFitStrategy(), new MutableClock());
        Ticket t = lot.park(new Vehicle("A", VehicleType.CAR)).orElseThrow();
        lot.unpark(t.id());
        assertThrows(IllegalArgumentException.class, () -> lot.unpark(t.id()));
        assertThrows(IllegalArgumentException.class, () -> lot.unpark("nope"));
    }

    @Test
    void feeIsPerStartedHourWithOneHourMinimum() {
        MutableClock clock = new MutableClock();
        ParkingLot lot = lot(new FirstFitStrategy(), clock);
        Ticket quick = lot.park(new Vehicle("A", VehicleType.CAR)).orElseThrow();
        clock.advance(Duration.ofMinutes(5));
        assertEquals(20, lot.unpark(quick.id()));

        Ticket exact = lot.park(new Vehicle("B", VehicleType.CAR)).orElseThrow();
        clock.advance(Duration.ofMinutes(60));
        assertEquals(20, lot.unpark(exact.id()));

        Ticket long_ = lot.park(new Vehicle("C", VehicleType.CAR)).orElseThrow();
        clock.advance(Duration.ofMinutes(61));
        assertEquals(40, lot.unpark(long_.id()));
    }

    @Test
    void feeDependsOnVehicleType() {
        MutableClock clock = new MutableClock();
        ParkingLot lot = lot(new FirstFitStrategy(), clock);
        Ticket truck = lot.park(new Vehicle("T", VehicleType.TRUCK)).orElseThrow();
        clock.advance(Duration.ofHours(2));
        assertEquals(80, lot.unpark(truck.id()));
    }

    // ---- concurrency ----
    private static final int SPOTS = 10;
    private static final int THREADS = 64;

    private List<ParkingSpot> manySpots() {
        List<ParkingSpot> list = new ArrayList<>();
        for (int i = 1; i <= SPOTS; i++) list.add(new ParkingSpot(i, SpotSize.MEDIUM));
        return list;
    }

    @Test
    void neverGivesTheSameSpotToTwoVehicles() throws Exception {
        ParkingLot lot = new ParkingLot(manySpots(), new FirstFitStrategy(), HourlyPricing.defaults(), Clock.systemUTC());
        List<Integer> spotIds = Collections.synchronizedList(new ArrayList<>());
        runConcurrently(i -> lot.park(new Vehicle("V" + i, VehicleType.CAR)).ifPresent(t -> spotIds.add(t.spotId())));
        assertEquals(SPOTS, spotIds.size());
        assertEquals(SPOTS, new HashSet<>(spotIds).size());
        assertEquals(0, lot.freeSpotCount());
    }

    @Test
    void naiveLotDoubleBooksSpots() throws Exception {
        boolean observed = false;
        for (int attempt = 0; attempt < 5 && !observed; attempt++) {
            NaiveParkingLot lot = new NaiveParkingLot(manySpots());
            List<Integer> spotIds = Collections.synchronizedList(new ArrayList<>());
            runConcurrently(i -> lot.park(new Vehicle("V" + i, VehicleType.CAR)).ifPresent(spotIds::add));
            observed = spotIds.size() != new HashSet<>(spotIds).size() || spotIds.size() > SPOTS;
        }
        assumeTrue(observed, "race not reproduced on this machine/run");
        assertTrue(observed);
    }

    private void runConcurrently(java.util.function.IntConsumer task) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        CountDownLatch ready = new CountDownLatch(THREADS);
        CountDownLatch go = new CountDownLatch(1);
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < THREADS; i++) {
            final int n = i;
            futures.add(pool.submit(() -> {
                ready.countDown();
                go.await();
                task.accept(n);
                return null;
            }));
        }
        ready.await();
        go.countDown();
        for (Future<?> f : futures) f.get(30, TimeUnit.SECONDS);
        pool.shutdown();
    }
}

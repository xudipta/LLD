package walkthroughs.parkinglot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import problems.parkinglot.BestFitStrategy;
import problems.parkinglot.HourlyPricing;
import problems.parkinglot.ParkingSpot;
import problems.parkinglot.SpotSize;
import problems.parkinglot.Vehicle;
import problems.parkinglot.VehicleType;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;

/** Same scenarios against step 1, step 2 and the final design in problems.parkinglot. */
class BehaviourTest {
    interface Lot {
        Optional<String> park(String type);   // "bike", "car" or "truck"

        long unpark(String ticket);
    }

    interface LotFactory {
        Lot create(int small, int medium, int large, Clock clock);
    }

    private static class MutableClock extends Clock {
        private Instant now = Instant.parse("2026-01-01T10:00:00Z");

        void advance(Duration d) { now = now.plus(d); }

        @Override public ZoneId getZone() { return ZoneOffset.UTC; }
        @Override public Clock withZone(ZoneId zone) { return this; }
        @Override public Instant instant() { return now; }
    }

    // ---- adapters ----
    static final LotFactory STEP1 = (s, m, l, clock) -> {
        walkthroughs.parkinglot.step1.ParkingLot lot = new walkthroughs.parkinglot.step1.ParkingLot(s, m, l);
        return new Lot() {
            public Optional<String> park(String type) { return Optional.ofNullable(lot.park(type)); }
            public long unpark(String ticket) { return lot.unpark(ticket); }
        };
    };

    static final LotFactory STEP2 = (s, m, l, clock) -> {
        walkthroughs.parkinglot.step2.ParkingLot lot = new walkthroughs.parkinglot.step2.ParkingLot(
                s, m, l, new walkthroughs.parkinglot.step2.HourlyPricing(), clock);
        return new Lot() {
            public Optional<String> park(String type) {
                return lot.park(walkthroughs.parkinglot.step2.VehicleType.valueOf(type.toUpperCase()));
            }
            public long unpark(String ticket) { return lot.unpark(ticket); }
        };
    };

    static final LotFactory FINAL = (s, m, l, clock) -> {
        List<ParkingSpot> spots = new ArrayList<>();
        int id = 1;
        for (int i = 0; i < s; i++) spots.add(new ParkingSpot(id++, SpotSize.SMALL));
        for (int i = 0; i < m; i++) spots.add(new ParkingSpot(id++, SpotSize.MEDIUM));
        for (int i = 0; i < l; i++) spots.add(new ParkingSpot(id++, SpotSize.LARGE));
        problems.parkinglot.ParkingLot lot = new problems.parkinglot.ParkingLot(spots, new BestFitStrategy(), HourlyPricing.defaults(), clock);
        int[] plate = {0};
        return new Lot() {
            public Optional<String> park(String type) {
                return lot.park(new Vehicle("P" + plate[0]++, VehicleType.valueOf(type.toUpperCase()))).map(t -> t.id());
            }
            public long unpark(String ticket) { return lot.unpark(ticket); }
        };
    };

    static Stream<org.junit.jupiter.params.provider.Arguments> allSteps() {
        return Stream.of(of("step 1: first version", STEP1), of("step 2: strategies and clock", STEP2), of("final: problems.parkinglot", FINAL));
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> stepsWithAClock() {
        return Stream.of(of("step 2: strategies and clock", STEP2), of("final: problems.parkinglot", FINAL));
    }

    private static Lot lot(LotFactory f, int s, int m, int l) {
        return f.create(s, m, l, Clock.systemUTC());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("allSteps")
    void aTruckOnlyFitsALargeSpot(String step, LotFactory f) {
        Lot lot = lot(f, 1, 1, 1);
        assertTrue(lot.park("truck").isPresent());
        assertTrue(lot.park("truck").isEmpty());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("allSteps")
    void smallVehiclesOverflowIntoBiggerSpotsButNotTheOtherWayRound(String step, LotFactory f) {
        Lot lot = lot(f, 1, 1, 1);
        assertTrue(lot.park("bike").isPresent());   // small
        assertTrue(lot.park("bike").isPresent());   // medium
        assertTrue(lot.park("car").isPresent());    // large
        assertTrue(lot.park("car").isEmpty());      // nothing left
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("allSteps")
    void aCarCannotTakeASmallSpot(String step, LotFactory f) {
        assertTrue(lot(f, 3, 0, 0).park("car").isEmpty());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("allSteps")
    void leavingFreesTheSpot(String step, LotFactory f) {
        Lot lot = lot(f, 0, 1, 0);
        String ticket = lot.park("car").orElseThrow();
        assertTrue(lot.park("car").isEmpty());
        lot.unpark(ticket);
        assertTrue(lot.park("car").isPresent());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("allSteps")
    void thereIsAOneHourMinimumFeePerVehicleType(String step, LotFactory f) {
        Lot lot = lot(f, 1, 1, 1);
        assertEquals(10, lot.unpark(lot.park("bike").orElseThrow()));
        assertEquals(20, lot.unpark(lot.park("car").orElseThrow()));
        assertEquals(40, lot.unpark(lot.park("truck").orElseThrow()));
    }

    /** Step 1 reads the wall clock directly, so this cannot even be written for it. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("stepsWithAClock")
    void feesFollowTheClock(String step, LotFactory f) {
        MutableClock clock = new MutableClock();
        Lot lot = f.create(0, 1, 0, clock);
        String ticket = lot.park("car").orElseThrow();
        clock.advance(Duration.ofMinutes(61));
        assertEquals(40, lot.unpark(ticket));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("stepsWithAClock")
    void anUnknownTicketIsRejected(String step, LotFactory f) {
        assertThrows(IllegalArgumentException.class, () -> lot(f, 1, 1, 1).unpark("nope"));
    }

    @Test
    void step1CrashesOnAnUnknownTicket() {
        // A smell that step 2 fixes: no validation, so a bad ticket is a NullPointerException.
        assertThrows(NullPointerException.class, () -> lot(STEP1, 1, 1, 1).unpark("nope"));
    }
}

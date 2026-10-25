package walkthroughs.parkinglot.step2;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * STEP 2 - an enum replaces the strings; pricing is a strategy; time comes from an injected Clock, so
 * fees can finally be tested. Still not thread-safe, and spot allocation is still fixed in the code.
 */
public class ParkingLot {
    private record Parked(VehicleType type, int spotSize, Instant since) {
    }

    private final int[] free;                      // free spots by size: small, medium, large
    private final PricingStrategy pricing;
    private final Clock clock;
    private final Map<String, Parked> tickets = new HashMap<>();
    private int next = 1;

    public ParkingLot(int small, int medium, int large, PricingStrategy pricing, Clock clock) {
        this.free = new int[]{small, medium, large};
        this.pricing = pricing;
        this.clock = clock;
    }

    public Optional<String> park(VehicleType type) {
        for (int size = type.minimumSpot; size < free.length; size++) {   // smallest spot that fits
            if (free[size] > 0) {
                free[size]--;
                String id = "T-" + next++;
                tickets.put(id, new Parked(type, size, clock.instant()));
                return Optional.of(id);
            }
        }
        return Optional.empty();
    }

    public long unpark(String ticket) {
        Parked p = tickets.remove(ticket);
        if (p == null) {
            throw new IllegalArgumentException("Unknown ticket: " + ticket);
        }
        free[p.spotSize()]++;
        return pricing.fee(p.type(), Duration.between(p.since(), clock.instant()));
    }
}

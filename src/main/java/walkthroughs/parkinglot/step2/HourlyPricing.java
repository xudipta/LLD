package walkthroughs.parkinglot.step2;

import java.time.Duration;

public class HourlyPricing implements PricingStrategy {
    private static final long[] RATE = {10, 20, 40};   // indexed by VehicleType ordinal

    @Override
    public long fee(VehicleType type, Duration parked) {
        long hours = Math.max(1, (parked.toMinutes() + 59) / 60);
        return hours * RATE[type.ordinal()];
    }
}

package problems.parkinglot;

import java.time.Duration;
import java.util.Map;

/** Rate per started hour, with a minimum of one hour. */
public class HourlyPricing implements PricingStrategy {
    private final Map<VehicleType, Long> ratePerHour;

    public HourlyPricing(Map<VehicleType, Long> ratePerHour) {
        this.ratePerHour = Map.copyOf(ratePerHour);
    }

    public static HourlyPricing defaults() {
        return new HourlyPricing(Map.of(VehicleType.BIKE, 10L, VehicleType.CAR, 20L, VehicleType.TRUCK, 40L));
    }

    @Override
    public long fee(VehicleType type, Duration parked) {
        long minutes = Math.max(parked.toMinutes() + (parked.toSecondsPart() > 0 ? 1 : 0), 1);
        long hours = (minutes + 59) / 60;
        return hours * ratePerHour.get(type);
    }
}

package problems.parkinglot;

import java.time.Duration;

public interface PricingStrategy {
    long fee(VehicleType type, Duration parked);
}

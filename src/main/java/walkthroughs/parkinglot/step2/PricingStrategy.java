package walkthroughs.parkinglot.step2;

import java.time.Duration;

public interface PricingStrategy {
    long fee(VehicleType type, Duration parked);
}

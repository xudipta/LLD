package problems.parkinglot;

import java.util.List;
import java.util.Optional;

/** Chooses one spot from the free spots that already fit the vehicle. */
public interface SpotAllocationStrategy {
    Optional<ParkingSpot> choose(List<ParkingSpot> candidates);
}

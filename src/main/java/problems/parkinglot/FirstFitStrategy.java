package problems.parkinglot;

import java.util.List;
import java.util.Optional;

/** Lowest id first, e.g. the spot closest to the entrance. */
public class FirstFitStrategy implements SpotAllocationStrategy {
    @Override
    public Optional<ParkingSpot> choose(List<ParkingSpot> candidates) {
        return candidates.stream().min((a, b) -> Integer.compare(a.getId(), b.getId()));
    }
}

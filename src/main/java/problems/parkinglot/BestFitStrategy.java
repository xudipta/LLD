package problems.parkinglot;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/** Smallest spot that fits, so big spots stay free for big vehicles. */
public class BestFitStrategy implements SpotAllocationStrategy {
    @Override
    public Optional<ParkingSpot> choose(List<ParkingSpot> candidates) {
        return candidates.stream()
                .min(Comparator.comparing(ParkingSpot::getSize).thenComparingInt(ParkingSpot::getId));
    }
}

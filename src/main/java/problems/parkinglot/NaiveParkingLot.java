package problems.parkinglot;

import java.util.List;
import java.util.Optional;

/**
 * Deliberately NOT thread-safe: finding a free spot and occupying it are separate
 * steps, so two threads can both see the same spot as free and both take it.
 */
public class NaiveParkingLot {
    private final List<ParkingSpot> spots;

    public NaiveParkingLot(List<ParkingSpot> spots) {
        this.spots = List.copyOf(spots);
    }

    /** @return the id of the spot given to the vehicle. */
    public Optional<Integer> park(Vehicle vehicle) {
        for (ParkingSpot spot : spots) {
            if (!spot.occupied && vehicle.type().fitsIn(spot.getSize())) { // check
                Thread.yield();                                            // widens the race window
                spot.occupied = true;                                      // act
                return Optional.of(spot.getId());
            }
        }
        return Optional.empty();
    }
}

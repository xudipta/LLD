package problems.elevator;

import java.util.Comparator;
import java.util.List;

public class NearestElevatorStrategy implements DispatchStrategy {
    @Override
    public Elevator choose(List<Elevator> elevators, int floor) {
        return elevators.stream()
                .min(Comparator.comparingInt((Elevator e) -> Math.abs(e.getCurrentFloor() - floor))
                        .thenComparingInt(Elevator::getId))
                .orElseThrow(() -> new IllegalStateException("No elevators"));
    }
}

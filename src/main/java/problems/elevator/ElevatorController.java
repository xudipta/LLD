package problems.elevator;

import java.util.List;

public class ElevatorController {
    private final List<Elevator> elevators;
    private final DispatchStrategy strategy;

    public ElevatorController(List<Elevator> elevators, DispatchStrategy strategy) {
        this.elevators = List.copyOf(elevators);
        this.strategy = strategy;
    }

    /** @return the id of the elevator sent to the floor */
    public int request(int floor) {
        Elevator chosen = strategy.choose(elevators, floor);
        chosen.requestStop(floor);
        return chosen.getId();
    }

    /** Advances every elevator by one tick. */
    public void tick() {
        elevators.forEach(Elevator::step);
    }
}

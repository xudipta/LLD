package problems.elevator;

import java.util.List;

public interface DispatchStrategy {
    Elevator choose(List<Elevator> elevators, int floor);
}

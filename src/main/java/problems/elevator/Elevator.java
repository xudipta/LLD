package problems.elevator;

import java.util.TreeSet;

/**
 * One car using the LOOK algorithm: keep going in the current direction while there are
 * stops ahead, serve them in floor order, then reverse. Driven one floor at a time by step().
 */
public class Elevator {
    private final int id;
    private final int topFloor;
    private final TreeSet<Integer> stops = new TreeSet<>();
    private int currentFloor;
    private Direction direction = Direction.IDLE;

    public Elevator(int id, int topFloor) {
        if (topFloor <= 0) {
            throw new IllegalArgumentException("topFloor must be positive");
        }
        this.id = id;
        this.topFloor = topFloor;
    }

    public void requestStop(int floor) {
        if (floor < 0 || floor > topFloor) {
            throw new IllegalArgumentException("No such floor: " + floor);
        }
        stops.add(floor);
    }

    public StepResult step() {
        if (stops.remove(currentFloor)) {              // a stop at the floor we are already on
            return new StepResult(currentFloor, direction, true);
        }
        if (stops.isEmpty()) {
            direction = Direction.IDLE;
            return new StepResult(currentFloor, direction, false);
        }
        direction = nextDirection();
        currentFloor += direction == Direction.UP ? 1 : -1;
        boolean stopped = stops.remove(currentFloor);
        return new StepResult(currentFloor, direction, stopped);
    }

    private Direction nextDirection() {
        Integer higher = stops.higher(currentFloor);
        Integer lower = stops.lower(currentFloor);
        if (direction == Direction.UP && higher != null) return Direction.UP;
        if (direction == Direction.DOWN && lower != null) return Direction.DOWN;
        // idle, or nothing left ahead: head for the nearer pending stop
        if (higher != null && (lower == null || higher - currentFloor <= currentFloor - lower)) {
            return Direction.UP;
        }
        return Direction.DOWN;
    }

    public int getId() { return id; }

    public int getCurrentFloor() { return currentFloor; }

    public Direction getDirection() { return direction; }

    public boolean isIdle() { return stops.isEmpty(); }
}

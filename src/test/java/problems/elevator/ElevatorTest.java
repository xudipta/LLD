package problems.elevator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorTest {
    /** Steps until idle and returns the floors where the doors opened, in order. */
    private List<Integer> runToCompletion(Elevator e) {
        List<Integer> served = new ArrayList<>();
        for (int i = 0; i < 100 && !e.isIdle(); i++) {
            StepResult r = e.step();
            if (r.stopped()) served.add(r.floor());
        }
        return served;
    }

    @Test
    void startsIdleOnTheGroundFloor() {
        Elevator e = new Elevator(1, 10);
        assertEquals(0, e.getCurrentFloor());
        assertEquals(Direction.IDLE, e.step().direction());
    }

    @Test
    void servesStopsInFloorOrderGoingUp() {
        Elevator e = new Elevator(1, 10);
        e.requestStop(5);
        e.requestStop(3);
        assertEquals(List.of(3, 5), runToCompletion(e));
    }

    @Test
    void keepsGoingUpBeforeReversing() {
        Elevator e = new Elevator(1, 10);
        e.requestStop(6);
        e.step();
        e.step();                       // now on floor 2, heading up
        e.requestStop(1);               // behind us
        e.requestStop(4);               // ahead of us
        assertEquals(List.of(4, 6, 1), runToCompletion(e));
    }

    @Test
    void servesDownwardStopsInDescendingOrder() {
        Elevator e = new Elevator(1, 10);
        e.requestStop(8);
        runToCompletion(e);
        e.requestStop(2);
        e.requestStop(5);
        assertEquals(List.of(5, 2), runToCompletion(e));
    }

    @Test
    void requestForTheCurrentFloorOpensTheDoorsWithoutMoving() {
        Elevator e = new Elevator(1, 10);
        e.requestStop(0);
        StepResult r = e.step();
        assertTrue(r.stopped());
        assertEquals(0, r.floor());
    }

    @Test
    void duplicateRequestsAreServedOnce() {
        Elevator e = new Elevator(1, 10);
        e.requestStop(3);
        e.requestStop(3);
        assertEquals(List.of(3), runToCompletion(e));
    }

    @Test
    void movesOneFloorPerStep() {
        Elevator e = new Elevator(1, 10);
        e.requestStop(3);
        assertEquals(1, e.step().floor());
        assertEquals(2, e.step().floor());
        assertEquals(3, e.step().floor());
    }

    @Test
    void returnsToIdleWhenDone() {
        Elevator e = new Elevator(1, 10);
        e.requestStop(2);
        runToCompletion(e);
        assertEquals(Direction.IDLE, e.step().direction());
    }

    @Test
    void rejectsFloorsOutsideTheBuilding() {
        Elevator e = new Elevator(1, 10);
        assertThrows(IllegalArgumentException.class, () -> e.requestStop(-1));
        assertThrows(IllegalArgumentException.class, () -> e.requestStop(11));
    }

    @Test
    void controllerSendsTheNearestElevator() {
        Elevator a = new Elevator(1, 10);
        Elevator b = new Elevator(2, 10);
        b.requestStop(8);
        for (int i = 0; i < 8; i++) b.step();      // b is on floor 8
        ElevatorController c = new ElevatorController(List.of(a, b), new NearestElevatorStrategy());
        assertEquals(2, c.request(9));
        assertEquals(1, c.request(1));
    }

    @Test
    void controllerBreaksTiesByLowestId() {
        ElevatorController c = new ElevatorController(
                List.of(new Elevator(1, 10), new Elevator(2, 10)), new NearestElevatorStrategy());
        assertEquals(1, c.request(4));
    }
}

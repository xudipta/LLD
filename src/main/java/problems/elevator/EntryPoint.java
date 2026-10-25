package problems.elevator;

public class EntryPoint {
    public static void main(String[] args) {
        Elevator car = new Elevator(1, 10);
        car.requestStop(4);
        car.requestStop(2);
        for (int tick = 0; tick < 6; tick++) {
            StepResult r = car.step();
            System.out.println("floor " + r.floor() + " " + r.direction() + (r.stopped() ? " [doors open]" : ""));
        }
    }
}

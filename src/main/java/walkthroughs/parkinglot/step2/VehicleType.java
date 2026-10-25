package walkthroughs.parkinglot.step2;

public enum VehicleType {
    BIKE(0), CAR(1), TRUCK(2);

    /** Index of the smallest spot size it fits: 0 small, 1 medium, 2 large. */
    final int minimumSpot;

    VehicleType(int minimumSpot) {
        this.minimumSpot = minimumSpot;
    }
}

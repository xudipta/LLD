package problems.parkinglot;

public enum VehicleType {
    BIKE(SpotSize.SMALL), CAR(SpotSize.MEDIUM), TRUCK(SpotSize.LARGE);

    private final SpotSize minimumSpot;

    VehicleType(SpotSize minimumSpot) {
        this.minimumSpot = minimumSpot;
    }

    /** A vehicle fits any spot at least this big. */
    public boolean fitsIn(SpotSize size) {
        return size.compareTo(minimumSpot) >= 0;
    }
}

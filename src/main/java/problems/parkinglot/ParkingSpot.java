package problems.parkinglot;

/** Occupancy is guarded by the owning lot's lock. */
public class ParkingSpot {
    private final int id;
    private final SpotSize size;
    boolean occupied;

    public ParkingSpot(int id, SpotSize size) {
        this.id = id;
        this.size = size;
    }

    public int getId() { return id; }

    public SpotSize getSize() { return size; }
}

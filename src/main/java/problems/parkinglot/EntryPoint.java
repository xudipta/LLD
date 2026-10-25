package problems.parkinglot;

import java.time.Clock;
import java.util.List;

public class EntryPoint {
    public static void main(String[] args) {
        ParkingLot lot = new ParkingLot(
                List.of(new ParkingSpot(1, SpotSize.SMALL), new ParkingSpot(2, SpotSize.MEDIUM), new ParkingSpot(3, SpotSize.LARGE)),
                new BestFitStrategy(), HourlyPricing.defaults(), Clock.systemUTC());
        Ticket t = lot.park(new Vehicle("KA-01-1234", VehicleType.CAR)).orElseThrow();
        System.out.println("Parked in spot " + t.spotId() + ", free spots: " + lot.freeSpotCount());
        System.out.println("Fee: " + lot.unpark(t.id()));
    }
}

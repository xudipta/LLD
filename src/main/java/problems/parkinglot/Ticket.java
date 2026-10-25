package problems.parkinglot;

import java.time.Instant;

public record Ticket(String id, Vehicle vehicle, int spotId, Instant entryTime) {
}

package problems.parkinglot;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe: choosing a free spot and marking it occupied happen under one lock,
 * so two vehicles can never be given the same spot. Pricing and allocation are
 * strategies; the clock is injected so fees are testable.
 */
public class ParkingLot {
    private final List<ParkingSpot> spots;
    private final SpotAllocationStrategy allocation;
    private final PricingStrategy pricing;
    private final Clock clock;
    private final Map<String, Ticket> ticketsById = new HashMap<>();
    private final Map<String, Ticket> ticketsByPlate = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();
    private long nextTicket = 1;

    public ParkingLot(List<ParkingSpot> spots, SpotAllocationStrategy allocation, PricingStrategy pricing, Clock clock) {
        this.spots = List.copyOf(spots);
        this.allocation = allocation;
        this.pricing = pricing;
        this.clock = clock;
    }

    /** @return a ticket, or empty if no suitable spot is free. */
    public Optional<Ticket> park(Vehicle vehicle) {
        lock.lock();
        try {
            if (ticketsByPlate.containsKey(vehicle.plate())) {
                throw new IllegalStateException("Vehicle already parked: " + vehicle.plate());
            }
            List<ParkingSpot> candidates = spots.stream()
                    .filter(s -> !s.occupied && vehicle.type().fitsIn(s.getSize()))
                    .toList();
            Optional<ParkingSpot> chosen = allocation.choose(candidates);
            if (chosen.isEmpty()) {
                return Optional.empty();
            }
            ParkingSpot spot = chosen.get();
            spot.occupied = true;
            Ticket ticket = new Ticket("T-" + nextTicket++, vehicle, spot.getId(), Instant.now(clock));
            ticketsById.put(ticket.id(), ticket);
            ticketsByPlate.put(vehicle.plate(), ticket);
            return Optional.of(ticket);
        } finally {
            lock.unlock();
        }
    }

    /** Frees the spot and returns the fee. */
    public long unpark(String ticketId) {
        lock.lock();
        try {
            Ticket ticket = ticketsById.remove(ticketId);
            if (ticket == null) {
                throw new IllegalArgumentException("Unknown ticket: " + ticketId);
            }
            ticketsByPlate.remove(ticket.vehicle().plate());
            spots.stream().filter(s -> s.getId() == ticket.spotId()).findFirst().orElseThrow().occupied = false;
            Duration parked = Duration.between(ticket.entryTime(), Instant.now(clock));
            return pricing.fee(ticket.vehicle().type(), parked);
        } finally {
            lock.unlock();
        }
    }

    public int freeSpotCount() {
        lock.lock();
        try {
            return (int) spots.stream().filter(s -> !s.occupied).count();
        } finally {
            lock.unlock();
        }
    }
}

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingLot {
    private final List<ParkingFloor> floors;
    private final Map<String, Ticket> activeTickets;
    private final ParkingStrategy parkingStrategy;
    private int ticketCounter;

    ParkingLot(List<ParkingFloor> floors, ParkingStrategy parkingStrategy) {
        this.floors = floors;
        this.parkingStrategy = parkingStrategy;
        this.activeTickets = new HashMap<>();
        this.ticketCounter = 0;
    }

    public Ticket findAndPark(Vehicle vehicle, LocalDateTime entryTime) {
        ParkingSpot spot = parkingStrategy.findSpot(floors, vehicle.getType());
        if (spot == null) {
            throw new IllegalStateException("No available spot for vehicle type: " + vehicle.getType());
        }

        spot.park(vehicle);

        // Find which floor this spot belongs to
        int floorNumber = -1;
        for (ParkingFloor floor : floors) {
            if (floor.getSpots().contains(spot)) {
                floorNumber = floor.getFloorNumber();
                break;
            }
        }

        ticketCounter++;
        String ticketId = "T-" + ticketCounter;
        Ticket ticket = new Ticket(ticketId, vehicle, spot, floorNumber, entryTime);
        activeTickets.put(ticketId, ticket);

        return ticket;
    }

    public Ticket getActiveTicket(String ticketId) {
        return activeTickets.get(ticketId);
    }

    public ParkingSpot unpark(String ticketId) {
        Ticket ticket = activeTickets.remove(ticketId);
        if (ticket == null) {
            throw new IllegalArgumentException("No active ticket found: " + ticketId);
        }

        ParkingSpot spot = ticket.getSpot();
        spot.unpark();
        return spot;
    }

    public void displayAvailability() {
        System.out.println("=== Parking Lot Availability ===");
        for (ParkingFloor floor : floors) {
            floor.displayAvailability();
        }
        System.out.println("================================");
    }

    public List<ParkingFloor> getFloors() {
        return floors;
    }
}

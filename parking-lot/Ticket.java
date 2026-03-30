import java.time.LocalDateTime;

public class Ticket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot spot;
    private final int floorNumber;
    private final LocalDateTime entryTime;

    public Ticket(String ticketId, Vehicle vehicle, ParkingSpot spot,
                  int floorNumber, LocalDateTime entryTime) {
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.spot = spot;
        this.floorNumber = floorNumber;
        this.entryTime = entryTime;
    }

    public String getTicketId() {
        return ticketId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getSpot() {
        return spot;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    @Override
    public String toString() {
        return "Ticket{id='" + ticketId + "', vehicle=" + vehicle
                + ", floor=" + floorNumber + ", spot=#" + spot.getSpotNumber()
                + ", entry=" + entryTime + "}";
    }
}

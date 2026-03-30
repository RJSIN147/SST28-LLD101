import java.time.LocalDateTime;

public class EntryGate {
    private final int gateNumber;
    private final ParkingLot parkingLot;

    public EntryGate(int gateNumber, ParkingLot parkingLot) {
        this.gateNumber = gateNumber;
        this.parkingLot = parkingLot;
    }

    public Ticket processEntry(Vehicle vehicle) {
        LocalDateTime entryTime = LocalDateTime.now();
        Ticket ticket = parkingLot.findAndPark(vehicle, entryTime);
        System.out.println("[EntryGate #" + gateNumber + "] Vehicle " + vehicle.getVehicleId()
                + " entered at " + entryTime + ". Ticket: " + ticket.getTicketId());
        return ticket;
    }

    public int getGateNumber() {
        return gateNumber;
    }
}

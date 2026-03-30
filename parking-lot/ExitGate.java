import java.time.LocalDateTime;

public class ExitGate {
    private final int gateNumber;
    private final ParkingLot parkingLot;
    private final FeeCalculator feeCalculator;

    public ExitGate(int gateNumber, ParkingLot parkingLot, FeeCalculator feeCalculator) {
        this.gateNumber = gateNumber;
        this.parkingLot = parkingLot;
        this.feeCalculator = feeCalculator;
    }

    public int processExit(String ticketId, LocalDateTime exitTime) {
        Ticket ticket = parkingLot.getActiveTicket(ticketId);
        if (ticket == null) {
            throw new IllegalArgumentException("Invalid ticket ID: " + ticketId);
        }

        int fee = feeCalculator.calculateFee(ticket, exitTime);
        parkingLot.unpark(ticketId);

        System.out.println("[ExitGate #" + gateNumber + "] Vehicle " + ticket.getVehicle().getVehicleId()
                + " exited at " + exitTime + ". Fee: " + fee);
        return fee;
    }

    public int getGateNumber() {
        return gateNumber;
    }
}

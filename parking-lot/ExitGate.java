import java.time.LocalDateTime;

public class ExitGate {
    private final int gateNumber;
    private final ParkingLot parkingLot;
    private final FeeCalculator feeCalculator;
    private final PaymentGateway paymentGateway;

    public ExitGate(int gateNumber, ParkingLot parkingLot,
                    FeeCalculator feeCalculator, PaymentGateway paymentGateway) {
        this.gateNumber = gateNumber;
        this.parkingLot = parkingLot;
        this.feeCalculator = feeCalculator;
        this.paymentGateway = paymentGateway;
    }

    public int processExit(String ticketId, LocalDateTime exitTime) {
        Ticket ticket = parkingLot.getActiveTicket(ticketId);
        if (ticket == null) {
            throw new IllegalArgumentException("Invalid ticket ID: " + ticketId);
        }

        // Calculate fee
        int fee = feeCalculator.calculateFee(ticket, exitTime);

        // Process payment
        boolean success = paymentGateway.processPayment(fee);
        if (!success) {
            throw new IllegalStateException("Payment failed for ticket " + ticketId
                    + ". Vehicle must remain parked.");
        }

        // Record payment details on ticket
        ticket.setFee(fee);
        ticket.setPaymentMethod(paymentGateway.getPaymentMethod());

        // Unpark vehicle
        parkingLot.unpark(ticketId);

        System.out.println("[ExitGate #" + gateNumber + "] Vehicle " + ticket.getVehicle().getVehicleId()
                + " exited. Fee: Rs." + fee + " (" + paymentGateway.getPaymentMethod() + ")");
        return fee;
    }

    public int getGateNumber() {
        return gateNumber;
    }
}


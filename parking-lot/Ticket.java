import java.time.LocalDateTime;

public class Ticket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot spot;
    private final int floorNumber;
    private final LocalDateTime entryTime;
    private int fee;
    private PaymentMethod paymentMethod;

    public Ticket(String ticketId, Vehicle vehicle, ParkingSpot spot,
                  int floorNumber, LocalDateTime entryTime) {
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.spot = spot;
        this.floorNumber = floorNumber;
        this.entryTime = entryTime;
        this.fee = 0;
        this.paymentMethod = null;
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

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        String base = "Ticket{id='" + ticketId + "', vehicle=" + vehicle
                + ", floor=" + floorNumber + ", spot=#" + spot.getSpotNumber()
                + ", entry=" + entryTime;
        if (paymentMethod != null) {
            base += ", fee=" + fee + ", paid=" + paymentMethod;
        }
        return base + "}";
    }
}

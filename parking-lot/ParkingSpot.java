public class ParkingSpot {
    private final int spotNumber;
    private final SpotType type;
    private Vehicle parkedVehicle;

    public ParkingSpot(int spotNumber, SpotType type) {
        this.spotNumber = spotNumber;
        this.type = type;
        this.parkedVehicle = null;
    }

    public boolean isAvailable() {
        return parkedVehicle == null;
    }

    public void park(Vehicle vehicle) {
        if (!isAvailable()) {
            throw new IllegalStateException("Spot " + spotNumber + " is already occupied.");
        }
        this.parkedVehicle = vehicle;
    }

    public Vehicle unpark() {
        if (isAvailable()) {
            throw new IllegalStateException("Spot " + spotNumber + " is already empty.");
        }
        Vehicle vehicle = this.parkedVehicle;
        this.parkedVehicle = null;
        return vehicle;
    }

    public int getSpotNumber() {
        return spotNumber;
    }

    public SpotType getType() {
        return type;
    }

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }

    @Override
    public String toString() {
        return "Spot{#" + spotNumber + ", type=" + type + ", available=" + isAvailable() + "}";
    }
}

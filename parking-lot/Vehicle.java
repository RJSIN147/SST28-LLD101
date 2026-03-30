public class Vehicle {
    private final int vehicleId;
    private final VehicleType type;

    public Vehicle(int vehicleId, VehicleType type) {
        this.vehicleId = vehicleId;
        this.type = type;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public VehicleType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Vehicle{id=" + vehicleId + ", type=" + type + "}";
    }
}

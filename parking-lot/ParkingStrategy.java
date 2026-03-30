import java.util.List;

public interface ParkingStrategy {
    ParkingSpot findSpot(List<ParkingFloor> floors, VehicleType vehicleType);
}

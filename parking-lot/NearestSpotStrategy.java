import java.util.List;

public class NearestSpotStrategy implements ParkingStrategy {

    @Override
    public ParkingSpot findSpot(List<ParkingFloor> floors, VehicleType vehicleType) {
        SpotType requiredType = VehicleTypeToSpotMapper.getRequiredSpotType(vehicleType);

        for (ParkingFloor floor : floors) {
            List<ParkingSpot> available = floor.getAvailableSpots(requiredType);
            if (!available.isEmpty()) {
                return available.get(0); // first available spot on the lowest floor
            }
        }
        return null; // no spot found
    }
}

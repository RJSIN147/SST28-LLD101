import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParkingFloor {
    private final int floorNumber;
    private final List<ParkingSpot> spots;

    public ParkingFloor(int floorNumber, List<ParkingSpot> spots) {
        this.floorNumber = floorNumber;
        this.spots = spots;
    }

    public List<ParkingSpot> getAvailableSpots(SpotType type) {
        return spots.stream()
                .filter(spot -> spot.getType() == type && spot.isAvailable())
                .collect(Collectors.toList());
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public List<ParkingSpot> getSpots() {
        return spots;
    }

    public void displayAvailability() {
        System.out.println("  Floor " + floorNumber + ":");
        for (SpotType type : SpotType.values()) {
            long available = spots.stream()
                    .filter(s -> s.getType() == type && s.isAvailable())
                    .count();
            long total = spots.stream()
                    .filter(s -> s.getType() == type)
                    .count();
            System.out.println("    " + type + ": " + available + "/" + total + " available");
        }
    }
}

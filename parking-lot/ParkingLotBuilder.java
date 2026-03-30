import java.util.ArrayList;
import java.util.List;

public class ParkingLotBuilder {
    private final List<ParkingFloor> floors;
    private ParkingStrategy parkingStrategy;
    private int floorCounter;

    public ParkingLotBuilder() {
        this.floors = new ArrayList<>();
        this.parkingStrategy = new NearestSpotStrategy(); // default
        this.floorCounter = 0;
    }

    public ParkingLotBuilder addFloor(int smallSpots, int mediumSpots, int largeSpots) {
        floorCounter++;
        List<ParkingSpot> spots = new ArrayList<>();
        int spotNumber = 1;

        for (int i = 0; i < smallSpots; i++) {
            spots.add(new ParkingSpot(spotNumber++, SpotType.SMALL));
        }
        for (int i = 0; i < mediumSpots; i++) {
            spots.add(new ParkingSpot(spotNumber++, SpotType.MEDIUM));
        }
        for (int i = 0; i < largeSpots; i++) {
            spots.add(new ParkingSpot(spotNumber++, SpotType.LARGE));
        }

        floors.add(new ParkingFloor(floorCounter, spots));
        return this;
    }

    public ParkingLotBuilder setParkingStrategy(ParkingStrategy parkingStrategy) {
        this.parkingStrategy = parkingStrategy;
        return this;
    }

    public ParkingLot build() {
        if (floors.isEmpty()) {
            throw new IllegalStateException("Parking lot must have at least one floor.");
        }
        return new ParkingLot(floors, parkingStrategy);
    }
}

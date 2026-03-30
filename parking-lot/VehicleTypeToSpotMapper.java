import java.util.HashMap;
import java.util.Map;

public class VehicleTypeToSpotMapper {
    private static final Map<VehicleType, SpotType> MAPPING = new HashMap<>();

    static {
        MAPPING.put(VehicleType.MOTORCYCLE, SpotType.SMALL);
        MAPPING.put(VehicleType.CAR, SpotType.MEDIUM);
        MAPPING.put(VehicleType.TRUCK, SpotType.LARGE);
    }

    public static SpotType getRequiredSpotType(VehicleType vehicleType) {
        SpotType spotType = MAPPING.get(vehicleType);
        if (spotType == null) {
            throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType);
        }
        return spotType;
    }
}

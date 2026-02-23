import java.util.*;

public class RoomPricingRegistry {
    private final Map<Integer, RoomPricing> map = new HashMap<>();
    private final RoomPricing defaultPricing;

    public RoomPricingRegistry(RoomPricing defaultPricing) {
        this.defaultPricing = defaultPricing;
    }

    public void register(int roomType, RoomPricing pricing) {
        map.put(roomType, pricing);
    }

    public RoomPricing getFor(int roomType) {
        return map.getOrDefault(roomType, defaultPricing);
    }
}

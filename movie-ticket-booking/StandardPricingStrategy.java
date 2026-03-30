import java.util.HashMap;
import java.util.Map;

public class StandardPricingStrategy implements PricingStrategy {
    private final Map<SeatType, Integer> prices;

    public StandardPricingStrategy() {
        prices = new HashMap<>();
        prices.put(SeatType.SILVER, 200);
        prices.put(SeatType.GOLD, 500);
        prices.put(SeatType.PLATINUM, 800);
    }

    public StandardPricingStrategy(Map<SeatType, Integer> customPrices) {
        this.prices = new HashMap<>(customPrices);
    }

    @Override
    public int calculatePrice(SeatType seatType) {
        Integer price = prices.get(seatType);
        if (price == null) {
            throw new IllegalArgumentException("No price defined for seat type: " + seatType);
        }
        return price;
    }
}

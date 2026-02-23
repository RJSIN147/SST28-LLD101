import java.util.*;

public class HostelFeeCalculator {
    private final RoomPricingRegistry roomPricingRegistry;
    private final Map<AddOn, AddOnPricing> addOnPricingMap;
    private final BookingRepo repo;

    public HostelFeeCalculator(RoomPricingRegistry roomPricingRegistry,
            Map<AddOn, AddOnPricing> addOnPricingMap,
            BookingRepo repo) {
        this.roomPricingRegistry = roomPricingRegistry;
        this.addOnPricingMap = addOnPricingMap;
        this.repo = repo;
    }

    public void process(BookingRequest req) {
        Money monthly = calculateMonthly(req);
        Money deposit = new Money(5000.00);

        ReceiptPrinter.print(req, monthly, deposit);

        String bookingId = "H-" + (7000 + new Random(1).nextInt(1000));
        repo.save(bookingId, req, monthly, deposit);
    }

    private Money calculateMonthly(BookingRequest req) {
        double base = roomPricingRegistry.getFor(req.roomType).basePrice();

        double add = 0.0;
        for (AddOn a : req.addOns) {
            AddOnPricing pricing = addOnPricingMap.get(a);
            if (pricing != null) {
                add += pricing.price();
            }
        }

        return new Money(base + add);
    }
}

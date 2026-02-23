import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Hostel Fee Calculator ===");

        // Room pricing registry (replaces switch-case)
        RoomPricingRegistry roomRegistry = new RoomPricingRegistry(new DeluxeRoomPricing());
        roomRegistry.register(LegacyRoomTypes.SINGLE, new SingleRoomPricing());
        roomRegistry.register(LegacyRoomTypes.DOUBLE, new DoubleRoomPricing());
        roomRegistry.register(LegacyRoomTypes.TRIPLE, new TripleRoomPricing());

        // Add-on pricing map (replaces if/else chain)
        Map<AddOn, AddOnPricing> addOnPricing = new HashMap<>();
        addOnPricing.put(AddOn.MESS, new MessPricing());
        addOnPricing.put(AddOn.LAUNDRY, new LaundryPricing());
        addOnPricing.put(AddOn.GYM, new GymPricing());

        BookingRepo repo = new FakeBookingRepo();
        HostelFeeCalculator calc = new HostelFeeCalculator(roomRegistry, addOnPricing, repo);

        BookingRequest req = new BookingRequest(LegacyRoomTypes.DOUBLE, List.of(AddOn.LAUNDRY, AddOn.MESS));
        calc.process(req);
    }
}

import java.time.LocalDateTime;

public class App {
    public static void main(String[] args) {
        // 1. Build a 2-floor parking lot
        ParkingLot parkingLot = new ParkingLotBuilder()
                .addFloor(2, 3, 1)  // Floor 1: 2 small, 3 medium, 1 large
                .addFloor(2, 3, 1)  // Floor 2: 2 small, 3 medium, 1 large
                .setParkingStrategy(new NearestSpotStrategy())
                .build();

        // 2. Create gates
        EntryGate entryGate = new EntryGate(1, parkingLot);
        ExitGate exitGate = new ExitGate(1, parkingLot, new HourlyFeeCalculator());

        // 3. Vehicles enter through the entry gate
        System.out.println("--- Vehicles Entering ---");
        Vehicle motorcycle = new Vehicle(101, VehicleType.MOTORCYCLE);
        Vehicle car = new Vehicle(202, VehicleType.CAR);
        Vehicle truck = new Vehicle(303, VehicleType.TRUCK);

        Ticket ticket1 = entryGate.processEntry(motorcycle);
        Ticket ticket2 = entryGate.processEntry(car);
        Ticket ticket3 = entryGate.processEntry(truck);

        // 4. Display availability after parking
        System.out.println("\n--- Availability After Parking ---");
        parkingLot.displayAvailability();

        // 5. Vehicles exit through the exit gate (simulate 2 hours later)
        System.out.println("\n--- Vehicles Exiting (2 hours later) ---");
        LocalDateTime exitTime = LocalDateTime.now().plusHours(2);

        int fee1 = exitGate.processExit(ticket1.getTicketId(), exitTime);
        int fee2 = exitGate.processExit(ticket2.getTicketId(), exitTime);
        int fee3 = exitGate.processExit(ticket3.getTicketId(), exitTime);

        System.out.println("\nFee Summary:");
        System.out.println("  Motorcycle (Vehicle " + motorcycle.getVehicleId() + "): " + fee1);
        System.out.println("  Car        (Vehicle " + car.getVehicleId() + "): " + fee2);
        System.out.println("  Truck      (Vehicle " + truck.getVehicleId() + "): " + fee3);

        // 6. Display availability after unparking
        System.out.println("\n--- Availability After Exiting ---");
        parkingLot.displayAvailability();

        // 7. Test parking full scenario
        System.out.println("\n--- Testing Spot Full Scenario ---");
        Vehicle moto1 = new Vehicle(401, VehicleType.MOTORCYCLE);
        Vehicle moto2 = new Vehicle(402, VehicleType.MOTORCYCLE);
        Vehicle moto3 = new Vehicle(403, VehicleType.MOTORCYCLE);
        Vehicle moto4 = new Vehicle(404, VehicleType.MOTORCYCLE);
        Vehicle moto5 = new Vehicle(405, VehicleType.MOTORCYCLE);

        entryGate.processEntry(moto1);
        entryGate.processEntry(moto2);
        entryGate.processEntry(moto3);
        entryGate.processEntry(moto4);

        try {
            entryGate.processEntry(moto5); // should fail — only 4 small spots total
        } catch (IllegalStateException e) {
            System.out.println("Expected error: " + e.getMessage());
        }
    }
}

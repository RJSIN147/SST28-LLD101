import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) throws InterruptedException {
        // 1. Build a 2-floor parking lot
        ParkingLot parkingLot = new ParkingLotBuilder()
                .addFloor(2, 3, 1)  // Floor 1: 2 small, 3 medium, 1 large
                .addFloor(2, 3, 1)  // Floor 2: 2 small, 3 medium, 1 large
                .setParkingStrategy(new NearestSpotStrategy())
                .build();

        // ============================================================
        // PART 1: Payment Gateway Demo (Cash, Debit Card, UPI)
        // ============================================================
        System.out.println("========================================");
        System.out.println("  PART 1: Payment Gateway Demo");
        System.out.println("========================================\n");

        // Entry gate
        EntryGate entryGate1 = new EntryGate(1, parkingLot);

        // Three exit gates with different payment methods
        ExitGate cashExit = new ExitGate(1, parkingLot, new HourlyFeeCalculator(), new CashPayment());
        ExitGate debitExit = new ExitGate(2, parkingLot, new HourlyFeeCalculator(), new DebitCardPayment());
        ExitGate upiExit = new ExitGate(3, parkingLot, new HourlyFeeCalculator(), new UPIPayment());

        // Vehicles enter
        Vehicle motorcycle = new Vehicle(101, VehicleType.MOTORCYCLE);
        Vehicle car = new Vehicle(202, VehicleType.CAR);
        Vehicle truck = new Vehicle(303, VehicleType.TRUCK);

        System.out.println("--- Vehicles Entering ---");
        Ticket t1 = entryGate1.processEntry(motorcycle);
        Ticket t2 = entryGate1.processEntry(car);
        Ticket t3 = entryGate1.processEntry(truck);

        parkingLot.displayAvailability();

        // Vehicles exit (2 hours later) with different payment methods
        System.out.println("\n--- Vehicles Exiting (2 hours later) ---");
        LocalDateTime exitTime = LocalDateTime.now().plusHours(2);

        cashExit.processExit(t1.getTicketId(), exitTime);   // Motorcycle pays cash
        debitExit.processExit(t2.getTicketId(), exitTime);   // Car pays debit card
        upiExit.processExit(t3.getTicketId(), exitTime);     // Truck pays UPI

        System.out.println("\n--- Ticket Receipts ---");
        System.out.println(t1);
        System.out.println(t2);
        System.out.println(t3);

        System.out.println();
        parkingLot.displayAvailability();

        // ============================================================
        // PART 2: Concurrent Entry (5 vehicles, 2 gates)
        // ============================================================
        System.out.println("\n==========================================");
        System.out.println("  PART 2: CONCURRENT ENTRY (5 vehicles)");
        System.out.println("==========================================\n");

        EntryGate gate1 = new EntryGate(1, parkingLot);
        EntryGate gate2 = new EntryGate(2, parkingLot);

        System.out.println("5 cars entering simultaneously through 2 gates...\n");

        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 1; i <= 5; i++) {
            final int id = 500 + i;
            final EntryGate gate = (i % 2 == 0) ? gate2 : gate1;
            executor.submit(() -> {
                try {
                    Vehicle v = new Vehicle(id, VehicleType.CAR);
                    Ticket t = gate.processEntry(v);
                    System.out.println("  >>> Vehicle " + id + " SUCCESS: " + t.getTicketId()
                            + " (Gate #" + gate.getGateNumber() + ")");
                } catch (Exception e) {
                    System.out.println("  >>> Vehicle " + id + " FAILED: " + e.getMessage());
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println();
        parkingLot.displayAvailability();

        // ============================================================
        // PART 3: Concurrent when nearly full
        // ============================================================
        System.out.println("\n==============================================");
        System.out.println("  PART 3: CONCURRENT NEAR-FULL (1 spot left)");
        System.out.println("==============================================\n");

        // Fill remaining medium spot (only 1 left)
        Vehicle car6 = new Vehicle(506, VehicleType.CAR);
        gate1.processEntry(car6);

        System.out.println("\nAll medium spots full. 3 cars try concurrently...\n");

        ExecutorService executor2 = Executors.newFixedThreadPool(3);
        for (int i = 1; i <= 3; i++) {
            final int id = 600 + i;
            executor2.submit(() -> {
                try {
                    Vehicle v = new Vehicle(id, VehicleType.CAR);
                    Ticket t = gate1.processEntry(v);
                    System.out.println("  >>> Vehicle " + id + " SUCCESS: " + t.getTicketId());
                } catch (Exception e) {
                    System.out.println("  >>> Vehicle " + id + " FAILED: " + e.getMessage());
                }
            });
        }
        executor2.shutdown();
        executor2.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println();
        parkingLot.displayAvailability();
    }
}

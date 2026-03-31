import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) throws InterruptedException {
        // ============================================================
        // PART 1: Single-Threaded Demo (original)
        // ============================================================
        System.out.println("========================================");
        System.out.println("  PART 1: Single-Threaded Demo");
        System.out.println("========================================\n");

        Building building = new BuildingBuilder()
                .setTotalFloors(10)
                .setNumberOfElevators(2)
                .setElevatorMaxWeight(400)
                .setDispatchStrategy(new NearestElevatorStrategy())
                .build();

        ElevatorController controller = building.getController();
        controller.addObserver(new ConsoleDisplay());

        System.out.println("=== Building Created: " + building + " ===\n");

        // External requests
        System.out.println("--- External Request: Floor 3, UP ---");
        Elevator e1 = controller.handleExternalRequest(3, Direction.UP);

        System.out.println("\n--- External Request: Floor 7, DOWN ---");
        Elevator e2 = controller.handleExternalRequest(7, Direction.DOWN);

        // Simulate steps
        System.out.println("\n=== Simulating Steps ===");
        int step = 0;
        while (controller.hasActiveRequests() && step < 20) {
            step++;
            System.out.println("\n-- Step " + step + " --");
            controller.step();
        }

        // Board passengers
        System.out.println("\n--- Passengers Boarding ---");
        Passenger p1 = new Passenger(1, 80, 8);
        Passenger p2 = new Passenger(2, 75, 5);
        Passenger p3 = new Passenger(3, 90, 1);

        System.out.println("Boarding on Elevator #" + e1.getElevatorId() + ":");
        e1.boardPassenger(p1);
        e1.boardPassenger(p2);

        System.out.println("\nBoarding on Elevator #" + e2.getElevatorId() + ":");
        e2.boardPassenger(p3);

        // Deliver passengers
        System.out.println("\n=== Delivering Passengers ===");
        step = 0;
        while (controller.hasActiveRequests() && step < 20) {
            step++;
            System.out.println("\n-- Step " + step + " --");
            controller.step();
        }

        System.out.println("\n=== Part 1 Complete ===");
        for (Elevator elev : controller.getElevators()) {
            System.out.println(elev);
        }

        // ============================================================
        // PART 2: Concurrent Button Presses
        // ============================================================
        System.out.println("\n==========================================");
        System.out.println("  PART 2: CONCURRENT BUTTON PRESSES");
        System.out.println("==========================================\n");

        Building building2 = new BuildingBuilder()
                .setTotalFloors(10)
                .setNumberOfElevators(3)
                .setElevatorMaxWeight(400)
                .setDispatchStrategy(new NearestElevatorStrategy())
                .build();

        ElevatorController ctrl2 = building2.getController();
        ctrl2.addObserver(new ConsoleDisplay());

        System.out.println("5 people press buttons on different floors simultaneously...\n");

        ExecutorService executor = Executors.newFixedThreadPool(5);
        executor.submit(() -> {
            Elevator e = ctrl2.handleExternalRequest(2, Direction.UP);
            System.out.println("  >>> Floor 2 UP → Elevator #" + (e != null ? e.getElevatorId() : "none"));
        });
        executor.submit(() -> {
            Elevator e = ctrl2.handleExternalRequest(8, Direction.DOWN);
            System.out.println("  >>> Floor 8 DOWN → Elevator #" + (e != null ? e.getElevatorId() : "none"));
        });
        executor.submit(() -> {
            Elevator e = ctrl2.handleExternalRequest(5, Direction.UP);
            System.out.println("  >>> Floor 5 UP → Elevator #" + (e != null ? e.getElevatorId() : "none"));
        });
        executor.submit(() -> {
            Elevator e = ctrl2.handleExternalRequest(1, Direction.UP);
            System.out.println("  >>> Floor 1 UP → Elevator #" + (e != null ? e.getElevatorId() : "none"));
        });
        executor.submit(() -> {
            Elevator e = ctrl2.handleExternalRequest(10, Direction.DOWN);
            System.out.println("  >>> Floor 10 DOWN → Elevator #" + (e != null ? e.getElevatorId() : "none"));
        });
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("\nAll requests dispatched. Running simulation...");
        step = 0;
        while (ctrl2.hasActiveRequests() && step < 20) {
            step++;
            System.out.println("\n-- Step " + step + " --");
            ctrl2.step();
        }

        System.out.println("\n=== After Concurrent Requests ===");
        for (Elevator elev : ctrl2.getElevators()) {
            System.out.println(elev);
        }
        System.out.println("Expected: All 5 requests handled, no crashes, no CME.");

        // ============================================================
        // PART 3: Concurrent Boarding (Weight Limit Test)
        // ============================================================
        System.out.println("\n==============================================");
        System.out.println("  PART 3: CONCURRENT BOARDING (Weight Test)");
        System.out.println("==============================================\n");

        Building building3 = new BuildingBuilder()
                .setTotalFloors(10)
                .setNumberOfElevators(1)
                .setElevatorMaxWeight(400)
                .setDispatchStrategy(new NearestElevatorStrategy())
                .build();

        ElevatorController ctrl3 = building3.getController();
        Elevator testElev = ctrl3.getElevator(1);

        System.out.println("Elevator limit: 400kg. 4 passengers (150kg each) try to board simultaneously...\n");

        ExecutorService executor2 = Executors.newFixedThreadPool(4);
        for (int i = 1; i <= 4; i++) {
            final int id = i;
            executor2.submit(() -> {
                Passenger p = new Passenger(id, 150, 5 + id);
                boolean success = testElev.boardPassenger(p);
                System.out.println("  >>> Passenger " + id + " (150kg) → "
                        + (success ? "BOARDED" : "REJECTED")
                        + " | Weight: " + testElev.getCurrentWeight() + "/" + testElev.getMaxWeightLimit() + "kg");
            });
        }
        executor2.shutdown();
        executor2.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("\nFinal: " + testElev);
        System.out.println("Expected: Exactly 2 BOARDED (300kg), 2 REJECTED (would exceed 400kg).");

        // Deliver the boarded passengers
        System.out.println("\n=== Delivering Boarded Passengers ===");
        step = 0;
        while (ctrl3.hasActiveRequests() && step < 20) {
            step++;
            System.out.println("\n-- Step " + step + " --");
            ctrl3.step();
        }

        System.out.println("\n=== All Tests Complete ===");
        System.out.println(testElev);
    }
}

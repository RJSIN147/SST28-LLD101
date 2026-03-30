public class App {
    public static void main(String[] args) {
        // 1. Build a 10-floor building with 2 elevators (400kg limit each)
        Building building = new BuildingBuilder()
                .setTotalFloors(10)
                .setNumberOfElevators(2)
                .setElevatorMaxWeight(400)
                .setDispatchStrategy(new NearestElevatorStrategy())
                .build();

        ElevatorController controller = building.getController();
        controller.addObserver(new ConsoleDisplay());

        System.out.println("=== Building Created: " + building + " ===\n");

        // 2. External request: someone on floor 3 presses UP
        System.out.println("--- External Request: Floor 3, UP ---");
        Elevator e1 = controller.handleExternalRequest(3, Direction.UP);

        // 3. External request: someone on floor 7 presses DOWN
        System.out.println("\n--- External Request: Floor 7, DOWN ---");
        Elevator e2 = controller.handleExternalRequest(7, Direction.DOWN);

        // 4. Simulate steps until elevators reach their targets
        System.out.println("\n=== Simulating Steps ===");
        int step = 0;
        while (controller.hasActiveRequests() && step < 20) {
            step++;
            System.out.println("\n-- Step " + step + " --");
            controller.step();
        }

        // 5. Passengers board at their floors
        System.out.println("\n--- Passengers Boarding ---");
        Passenger p1 = new Passenger(1, 80, 8);   // 80kg, wants floor 8
        Passenger p2 = new Passenger(2, 75, 5);   // 75kg, wants floor 5
        Passenger p3 = new Passenger(3, 90, 1);   // 90kg, wants floor 1

        System.out.println("Boarding on Elevator #" + e1.getElevatorId() + ":");
        e1.boardPassenger(p1);
        e1.boardPassenger(p2);

        System.out.println("\nBoarding on Elevator #" + e2.getElevatorId() + ":");
        e2.boardPassenger(p3);

        // 6. Simulate steps — elevators deliver passengers
        System.out.println("\n=== Delivering Passengers ===");
        step = 0;
        while (controller.hasActiveRequests() && step < 20) {
            step++;
            System.out.println("\n-- Step " + step + " --");
            controller.step();
        }

        // 7. Test overload scenario
        System.out.println("\n=== Overload Test ===");
        Elevator testElev = controller.getElevator(1);
        Passenger heavy1 = new Passenger(4, 150, 10);
        Passenger heavy2 = new Passenger(5, 150, 9);
        Passenger heavy3 = new Passenger(6, 150, 8);

        testElev.boardPassenger(heavy1);  // 150kg — OK
        testElev.boardPassenger(heavy2);  // 300kg — OK
        testElev.boardPassenger(heavy3);  // 450kg — EXCEEDS 400kg limit!

        // 8. Final simulation
        System.out.println("\n=== Final Delivery ===");
        step = 0;
        while (controller.hasActiveRequests() && step < 20) {
            step++;
            System.out.println("\n-- Step " + step + " --");
            controller.step();
        }

        System.out.println("\n=== All Requests Served ===");
        for (Elevator elev : controller.getElevators()) {
            System.out.println(elev);
        }
    }
}

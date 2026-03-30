public class Building {
    private final int totalFloors;
    private final ElevatorController controller;

    Building(int totalFloors, ElevatorController controller) {
        this.totalFloors = totalFloors;
        this.controller = controller;
    }

    public int getTotalFloors() {
        return totalFloors;
    }

    public ElevatorController getController() {
        return controller;
    }

    @Override
    public String toString() {
        return "Building{floors=" + totalFloors + ", elevators="
                + controller.getElevators().size() + "}";
    }
}

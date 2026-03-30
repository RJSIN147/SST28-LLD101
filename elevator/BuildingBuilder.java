import java.util.ArrayList;
import java.util.List;

public class BuildingBuilder {
    private int totalFloors = 10;
    private int numberOfElevators = 2;
    private int elevatorMaxWeight = 400;
    private DispatchStrategy dispatchStrategy = new NearestElevatorStrategy();

    public BuildingBuilder setTotalFloors(int totalFloors) {
        this.totalFloors = totalFloors;
        return this;
    }

    public BuildingBuilder setNumberOfElevators(int numberOfElevators) {
        this.numberOfElevators = numberOfElevators;
        return this;
    }

    public BuildingBuilder setElevatorMaxWeight(int elevatorMaxWeight) {
        this.elevatorMaxWeight = elevatorMaxWeight;
        return this;
    }

    public BuildingBuilder setDispatchStrategy(DispatchStrategy dispatchStrategy) {
        this.dispatchStrategy = dispatchStrategy;
        return this;
    }

    public Building build() {
        if (totalFloors < 2) {
            throw new IllegalStateException("Building must have at least 2 floors.");
        }
        if (numberOfElevators < 1) {
            throw new IllegalStateException("Building must have at least 1 elevator.");
        }

        List<Elevator> elevators = new ArrayList<>();
        for (int i = 1; i <= numberOfElevators; i++) {
            elevators.add(new Elevator(i, elevatorMaxWeight));
        }

        ElevatorController controller = new ElevatorController(elevators, dispatchStrategy);
        return new Building(totalFloors, controller);
    }
}

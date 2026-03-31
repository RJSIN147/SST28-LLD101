import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ElevatorController {
    private final List<Elevator> elevators;
    private final DispatchStrategy dispatchStrategy;
    private final List<ElevatorObserver> observers;

    public ElevatorController(List<Elevator> elevators, DispatchStrategy dispatchStrategy) {
        this.elevators = elevators;
        this.dispatchStrategy = dispatchStrategy;
        this.observers = new CopyOnWriteArrayList<>();
    }

    public void addObserver(ElevatorObserver observer) {
        observers.add(observer);
    }

    public Elevator handleExternalRequest(int floor, Direction direction) {
        Request request = Request.external(floor, direction);
        Elevator selected = dispatchStrategy.selectElevator(elevators, request);
        if (selected != null) {
            System.out.println("[Controller] Dispatching Elevator #" + selected.getElevatorId()
                    + " for external request at floor " + floor + " (" + direction + ")");
            selected.addRequest(floor);
        } else {
            System.out.println("[Controller] No elevator available for request: " + request);
        }
        return selected;
    }

    public void handleInternalRequest(int elevatorId, int floor) {
        for (Elevator elevator : elevators) {
            if (elevator.getElevatorId() == elevatorId) {
                System.out.println("[Controller] Internal request: Elevator #" + elevatorId
                        + " -> floor " + floor);
                elevator.addRequest(floor);
                return;
            }
        }
        System.out.println("[Controller] Elevator #" + elevatorId + " not found.");
    }

    public void step() {
        for (Elevator elevator : elevators) {
            elevator.move();
            notifyObservers(elevator);
        }
    }

    public boolean hasActiveRequests() {
        for (Elevator elevator : elevators) {
            if (elevator.hasRequests()) {
                return true;
            }
        }
        return false;
    }

    private void notifyObservers(Elevator elevator) {
        for (ElevatorObserver observer : observers) {
            observer.onStateChange(elevator);
        }
    }

    public List<Elevator> getElevators() {
        return elevators;
    }

    public Elevator getElevator(int elevatorId) {
        for (Elevator elevator : elevators) {
            if (elevator.getElevatorId() == elevatorId) {
                return elevator;
            }
        }
        return null;
    }
}

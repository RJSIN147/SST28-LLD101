import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantLock;

public class Elevator {
    private final int elevatorId;
    private int currentFloor;
    private ElevatorState state;
    private DoorState doorState;
    private final int maxWeightLimit;
    private int currentWeight;
    private final TreeSet<Integer> upRequests;
    private final TreeSet<Integer> downRequests;
    private final List<Passenger> passengers;
    private final ReentrantLock lock = new ReentrantLock(true);

    public Elevator(int elevatorId, int maxWeightLimit) {
        this.elevatorId = elevatorId;
        this.currentFloor = 1;
        this.state = new IdleState();
        this.doorState = DoorState.CLOSED;
        this.maxWeightLimit = maxWeightLimit;
        this.currentWeight = 0;
        this.upRequests = new TreeSet<>();
        this.downRequests = new TreeSet<>();
        this.passengers = new ArrayList<>();
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    public void addRequest(int floor) {
        lock.lock();
        try {
            state.handleRequest(this, floor);
        } finally {
            lock.unlock();
        }
    }

    public void move() {
        lock.lock();
        try {
            state.move(this);
        } finally {
            lock.unlock();
        }
    }

    // --- Passenger boarding ---

    public boolean boardPassenger(Passenger passenger) {
        lock.lock();
        try {
            if (isOverloaded(passenger.getWeight())) {
                System.out.println("  [Elevator #" + elevatorId + "] OVERLOADED! Cannot board "
                        + passenger + ". Current: " + currentWeight + "kg, Limit: " + maxWeightLimit + "kg");
                return false;
            }
            passengers.add(passenger);
            currentWeight += passenger.getWeight();
            addRequestInternal(passenger.getDestinationFloor());
            System.out.println("  [Elevator #" + elevatorId + "] Boarded " + passenger
                    + ". Weight: " + currentWeight + "/" + maxWeightLimit + "kg");
            return true;
        } finally {
            lock.unlock();
        }
    }

    public void deboardPassengers() {
        // Called from openDoor() which is called from move() — lock already held
        List<Passenger> toRemove = new ArrayList<>();
        for (Passenger p : passengers) {
            if (p.getDestinationFloor() == currentFloor) {
                toRemove.add(p);
                currentWeight -= p.getWeight();
                System.out.println("  [Elevator #" + elevatorId + "] Deboarded " + p
                        + ". Weight: " + currentWeight + "/" + maxWeightLimit + "kg");
            }
        }
        passengers.removeAll(toRemove);
    }

    public boolean isOverloaded(int additionalWeight) {
        return (currentWeight + additionalWeight) > maxWeightLimit;
    }

    // --- Internal request (called when lock is already held) ---

    private void addRequestInternal(int floor) {
        state.handleRequest(this, floor);
    }

    // --- Movement helpers (called by states — lock already held) ---

    public void moveUp() {
        currentFloor++;
    }

    public void moveDown() {
        currentFloor--;
    }

    public void openDoor() {
        doorState = DoorState.OPEN;
        deboardPassengers();
    }

    public void closeDoor() {
        doorState = DoorState.CLOSED;
    }

    // --- State management ---

    public void setState(ElevatorState state) {
        this.state = state;
    }

    public void addToUpRequests(int floor) {
        upRequests.add(floor);
    }

    public void addToDownRequests(int floor) {
        downRequests.add(floor);
    }

    // --- Getters ---

    public int getElevatorId() {
        return elevatorId;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Direction getDirection() {
        return state.getDirection();
    }

    public DoorState getDoorState() {
        return doorState;
    }

    public int getMaxWeightLimit() {
        return maxWeightLimit;
    }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public TreeSet<Integer> getUpRequests() {
        return upRequests;
    }

    public TreeSet<Integer> getDownRequests() {
        return downRequests;
    }

    public boolean hasRequests() {
        return !upRequests.isEmpty() || !downRequests.isEmpty();
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    @Override
    public String toString() {
        return "Elevator{#" + elevatorId + ", floor=" + currentFloor
                + ", dir=" + getDirection() + ", door=" + doorState
                + ", weight=" + currentWeight + "/" + maxWeightLimit + "kg"
                + ", passengers=" + passengers.size() + "}";
    }
}


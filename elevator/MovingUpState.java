public class MovingUpState implements ElevatorState {

    @Override
    public void handleRequest(Elevator elevator, int floor) {
        if (floor >= elevator.getCurrentFloor()) {
            elevator.addToUpRequests(floor);
        } else {
            elevator.addToDownRequests(floor);
        }
    }

    @Override
    public void move(Elevator elevator) {
        // Check if current floor is a stop
        if (elevator.getUpRequests().contains(elevator.getCurrentFloor())) {
            elevator.getUpRequests().remove(elevator.getCurrentFloor());
            elevator.openDoor();
        }

        // If more up requests, move up
        if (!elevator.getUpRequests().isEmpty()) {
            elevator.closeDoor();
            elevator.moveUp();
        }
        // No more up requests — switch direction or go idle
        else if (!elevator.getDownRequests().isEmpty()) {
            elevator.closeDoor();
            elevator.setState(new MovingDownState());
        } else {
            elevator.closeDoor();
            elevator.setState(new IdleState());
        }
    }

    @Override
    public Direction getDirection() {
        return Direction.UP;
    }

    @Override
    public String toString() {
        return "MOVING_UP";
    }
}

public class MovingDownState implements ElevatorState {

    @Override
    public void handleRequest(Elevator elevator, int floor) {
        if (floor <= elevator.getCurrentFloor()) {
            elevator.addToDownRequests(floor);
        } else {
            elevator.addToUpRequests(floor);
        }
    }

    @Override
    public void move(Elevator elevator) {
        // Check if current floor is a stop
        if (elevator.getDownRequests().contains(elevator.getCurrentFloor())) {
            elevator.getDownRequests().remove(elevator.getCurrentFloor());
            elevator.openDoor();
        }

        // If more down requests, move down
        if (!elevator.getDownRequests().isEmpty()) {
            elevator.closeDoor();
            elevator.moveDown();
        }
        // No more down requests — switch direction or go idle
        else if (!elevator.getUpRequests().isEmpty()) {
            elevator.closeDoor();
            elevator.setState(new MovingUpState());
        } else {
            elevator.closeDoor();
            elevator.setState(new IdleState());
        }
    }

    @Override
    public Direction getDirection() {
        return Direction.DOWN;
    }

    @Override
    public String toString() {
        return "MOVING_DOWN";
    }
}

public class IdleState implements ElevatorState {

    @Override
    public void handleRequest(Elevator elevator, int floor) {
        if (floor == elevator.getCurrentFloor()) {
            elevator.openDoor();
            return;
        }

        if (floor > elevator.getCurrentFloor()) {
            elevator.addToUpRequests(floor);
            elevator.setState(new MovingUpState());
        } else {
            elevator.addToDownRequests(floor);
            elevator.setState(new MovingDownState());
        }
    }

    @Override
    public void move(Elevator elevator) {
        // Idle — no movement. Check if there are pending requests.
        if (!elevator.getUpRequests().isEmpty()) {
            elevator.setState(new MovingUpState());
        } else if (!elevator.getDownRequests().isEmpty()) {
            elevator.setState(new MovingDownState());
        }
    }

    @Override
    public Direction getDirection() {
        return Direction.IDLE;
    }

    @Override
    public String toString() {
        return "IDLE";
    }
}

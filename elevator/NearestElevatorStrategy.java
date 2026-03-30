import java.util.List;

public class NearestElevatorStrategy implements DispatchStrategy {

    @Override
    public Elevator selectElevator(List<Elevator> elevators, Request request) {
        Elevator best = null;
        int bestScore = Integer.MAX_VALUE;

        for (Elevator elevator : elevators) {
            int distance = Math.abs(elevator.getCurrentFloor() - request.getFloor());
            int score;

            Direction elevDir = elevator.getDirection();

            if (elevDir == Direction.IDLE) {
                // Idle elevators are preferred — score = distance
                score = distance;
            } else if (elevDir == request.getDirection()) {
                // Moving in same direction and hasn't passed the floor yet
                boolean approaching;
                if (elevDir == Direction.UP) {
                    approaching = elevator.getCurrentFloor() <= request.getFloor();
                } else {
                    approaching = elevator.getCurrentFloor() >= request.getFloor();
                }
                score = approaching ? distance : distance + 1000;
            } else {
                // Moving in opposite direction — least preferred
                score = distance + 2000;
            }

            if (score < bestScore) {
                bestScore = score;
                best = elevator;
            }
        }

        return best;
    }
}

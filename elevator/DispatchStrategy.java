import java.util.List;

public interface DispatchStrategy {
    Elevator selectElevator(List<Elevator> elevators, Request request);
}

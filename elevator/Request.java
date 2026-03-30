public class Request {
    private final int floor;
    private final Direction direction;
    private final RequestType type;
    private final int elevatorId; // used only for INTERNAL requests

    public Request(int floor, Direction direction, RequestType type, int elevatorId) {
        this.floor = floor;
        this.direction = direction;
        this.type = type;
        this.elevatorId = elevatorId;
    }

    public static Request external(int floor, Direction direction) {
        return new Request(floor, direction, RequestType.EXTERNAL, -1);
    }

    public static Request internal(int elevatorId, int floor) {
        return new Request(floor, Direction.IDLE, RequestType.INTERNAL, elevatorId);
    }

    public int getFloor() {
        return floor;
    }

    public Direction getDirection() {
        return direction;
    }

    public RequestType getType() {
        return type;
    }

    public int getElevatorId() {
        return elevatorId;
    }

    @Override
    public String toString() {
        if (type == RequestType.EXTERNAL) {
            return "Request{EXTERNAL, floor=" + floor + ", dir=" + direction + "}";
        }
        return "Request{INTERNAL, elevator=" + elevatorId + ", floor=" + floor + "}";
    }
}

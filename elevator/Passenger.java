public class Passenger {
    private final int passengerId;
    private final int weight;
    private final int destinationFloor;

    public Passenger(int passengerId, int weight, int destinationFloor) {
        this.passengerId = passengerId;
        this.weight = weight;
        this.destinationFloor = destinationFloor;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public int getWeight() {
        return weight;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    @Override
    public String toString() {
        return "Passenger{id=" + passengerId + ", weight=" + weight + "kg, dest=floor " + destinationFloor + "}";
    }
}

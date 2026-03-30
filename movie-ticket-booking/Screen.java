import java.util.List;
import java.util.stream.Collectors;

public class Screen {
    private final int screenNumber;
    private final List<Seat> seats;

    public Screen(int screenNumber, List<Seat> seats) {
        this.screenNumber = screenNumber;
        this.seats = seats;
    }

    public int getScreenNumber() {
        return screenNumber;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public List<Seat> getSeatsByType(SeatType type) {
        return seats.stream()
                .filter(seat -> seat.getType() == type)
                .collect(Collectors.toList());
    }

    public Seat getSeatByNumber(int seatNumber) {
        return seats.stream()
                .filter(s -> s.getSeatNumber() == seatNumber)
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return "Screen{#" + screenNumber + ", seats=" + seats.size() + "}";
    }
}

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Show {
    private final int showId;
    private final Movie movie;
    private final Screen screen;
    private final LocalDateTime showTime;
    private final Set<Integer> bookedSeatNumbers;
    private final ReentrantLock lock = new ReentrantLock(true); // fair lock

    Show(int showId, Movie movie, Screen screen, LocalDateTime showTime) {
        this.showId = showId;
        this.movie = movie;
        this.screen = screen;
        this.showTime = showTime;
        this.bookedSeatNumbers = new HashSet<>();
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    public boolean isAvailable(int seatNumber) {
        return !bookedSeatNumbers.contains(seatNumber);
    }

    public void bookSeat(int seatNumber) {
        if (!isAvailable(seatNumber)) {
            throw new IllegalStateException("Seat #" + seatNumber + " is already booked for this show.");
        }
        Seat seat = screen.getSeatByNumber(seatNumber);
        if (seat == null) {
            throw new IllegalArgumentException("Seat #" + seatNumber + " does not exist on screen #" + screen.getScreenNumber());
        }
        bookedSeatNumbers.add(seatNumber);
    }

    public void releaseSeat(int seatNumber) {
        if (isAvailable(seatNumber)) {
            throw new IllegalStateException("Seat #" + seatNumber + " is not booked.");
        }
        bookedSeatNumbers.remove(seatNumber);
    }

    public List<Seat> getAvailableSeats() {
        return screen.getSeats().stream()
                .filter(seat -> !bookedSeatNumbers.contains(seat.getSeatNumber()))
                .collect(Collectors.toList());
    }

    public int getShowId() {
        return showId;
    }

    public Movie getMovie() {
        return movie;
    }

    public Screen getScreen() {
        return screen;
    }

    public LocalDateTime getShowTime() {
        return showTime;
    }

    public void displayAvailability() {
        System.out.println("Show: " + movie.getTitle() + " | Screen #" + screen.getScreenNumber()
                + " | " + showTime);
        List<Seat> available = getAvailableSeats();
        int total = screen.getSeats().size();
        System.out.println("  Available: " + available.size() + "/" + total);
        for (SeatType type : SeatType.values()) {
            long avail = available.stream().filter(s -> s.getType() == type).count();
            long typeTotal = screen.getSeatsByType(type).size();
            System.out.println("    " + type + ": " + avail + "/" + typeTotal);
        }
    }

    @Override
    public String toString() {
        return "Show{id=" + showId + ", movie='" + movie.getTitle() + "', screen=#"
                + screen.getScreenNumber() + ", time=" + showTime + "}";
    }
}

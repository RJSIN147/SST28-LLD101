import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingService {
    private final PricingStrategy pricingStrategy;
    private final List<BookingObserver> observers;
    private final Map<String, Booking> bookings;
    private int bookingCounter;

    public BookingService(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
        this.observers = new ArrayList<>();
        this.bookings = new HashMap<>();
        this.bookingCounter = 0;
    }

    public void addObserver(BookingObserver observer) {
        observers.add(observer);
    }

    public Booking book(int userId, Show show, List<Integer> seatNumbers) {
        // Validate all seats are available before booking any
        for (int seatNumber : seatNumbers) {
            if (!show.isAvailable(seatNumber)) {
                throw new IllegalStateException("Seat #" + seatNumber
                        + " is already booked for show: " + show.getMovie().getTitle());
            }
        }

        // Book seats and collect Seat objects
        List<Seat> bookedSeats = new ArrayList<>();
        int totalPrice = 0;

        for (int seatNumber : seatNumbers) {
            show.bookSeat(seatNumber);
            Seat seat = show.getScreen().getSeatByNumber(seatNumber);
            bookedSeats.add(seat);
            totalPrice += pricingStrategy.calculatePrice(seat.getType());
        }

        // Create booking
        bookingCounter++;
        String bookingId = "BK-" + bookingCounter;
        Booking booking = new Booking(bookingId, userId, show, bookedSeats,
                totalPrice, LocalDateTime.now());
        bookings.put(bookingId, booking);

        // Notify observers
        for (BookingObserver observer : observers) {
            observer.onBookingConfirmed(booking);
        }

        return booking;
    }

    public Booking cancel(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("No booking found: " + bookingId);
        }
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking " + bookingId + " is already cancelled.");
        }

        // Release seats
        Show show = booking.getShow();
        for (Seat seat : booking.getSeats()) {
            show.releaseSeat(seat.getSeatNumber());
        }

        booking.setStatus(BookingStatus.CANCELLED);

        // Notify observers
        for (BookingObserver observer : observers) {
            observer.onBookingCancelled(booking);
        }

        return booking;
    }

    public Booking getBooking(String bookingId) {
        return bookings.get(bookingId);
    }
}

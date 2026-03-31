import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class BookingService {
    private final PricingStrategy pricingStrategy;
    private final PaymentGateway paymentGateway;
    private final List<BookingObserver> observers;
    private final ConcurrentHashMap<String, Booking> bookings;
    private final AtomicInteger bookingCounter;

    public BookingService(PricingStrategy pricingStrategy, PaymentGateway paymentGateway) {
        this.pricingStrategy = pricingStrategy;
        this.paymentGateway = paymentGateway;
        this.observers = new CopyOnWriteArrayList<>();
        this.bookings = new ConcurrentHashMap<>();
        this.bookingCounter = new AtomicInteger(0);
    }

    public void addObserver(BookingObserver observer) {
        observers.add(observer);
    }

    public Booking book(int userId, Show show, List<Integer> seatNumbers) {
        show.lock();
        try {
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

            // Process payment
            boolean paymentSuccess = paymentGateway.processPayment(userId, totalPrice);
            if (!paymentSuccess) {
                // Rollback seats if payment fails
                for (Seat seat : bookedSeats) {
                    show.releaseSeat(seat.getSeatNumber());
                }
                throw new IllegalStateException("Payment failed for User " + userId
                        + ". Seats released.");
            }

            // Create booking
            String bookingId = "BK-" + bookingCounter.incrementAndGet();
            Booking booking = new Booking(bookingId, userId, show, bookedSeats,
                    totalPrice, LocalDateTime.now(), paymentGateway.getPaymentMethod());
            bookings.put(bookingId, booking);

            // Notify observers
            for (BookingObserver observer : observers) {
                observer.onBookingConfirmed(booking);
            }

            return booking;
        } finally {
            show.unlock();
        }
    }

    public Booking cancel(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("No booking found: " + bookingId);
        }
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking " + bookingId + " is already cancelled.");
        }

        Show show = booking.getShow();
        show.lock();
        try {
            // Release seats
            for (Seat seat : booking.getSeats()) {
                show.releaseSeat(seat.getSeatNumber());
            }

            booking.setStatus(BookingStatus.CANCELLED);

            // Notify observers
            for (BookingObserver observer : observers) {
                observer.onBookingCancelled(booking);
            }

            return booking;
        } finally {
            show.unlock();
        }
    }

    public Booking getBooking(String bookingId) {
        return bookings.get(bookingId);
    }
}

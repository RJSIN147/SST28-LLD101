import java.time.LocalDateTime;
import java.util.List;

public class Booking {
    private final String bookingId;
    private final int userId;
    private final Show show;
    private final List<Seat> seats;
    private final int totalPrice;
    private final LocalDateTime bookingTime;
    private final PaymentMethod paymentMethod;
    private BookingStatus status;

    public Booking(String bookingId, int userId, Show show, List<Seat> seats,
                   int totalPrice, LocalDateTime bookingTime, PaymentMethod paymentMethod) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.show = show;
        this.seats = seats;
        this.totalPrice = totalPrice;
        this.bookingTime = bookingTime;
        this.paymentMethod = paymentMethod;
        this.status = BookingStatus.CONFIRMED;
    }

    public String getBookingId() {
        return bookingId;
    }

    public int getUserId() {
        return userId;
    }

    public Show getShow() {
        return show;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Booking{id='" + bookingId + "', userId=" + userId
                + ", movie='" + show.getMovie().getTitle() + "', seats=" + seats.size()
                + ", total=" + totalPrice + ", paid=" + paymentMethod
                + ", status=" + status + "}";
    }
}

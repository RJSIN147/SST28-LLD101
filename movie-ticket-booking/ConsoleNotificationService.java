public class ConsoleNotificationService implements BookingObserver {

    @Override
    public void onBookingConfirmed(Booking booking) {
        System.out.println("[NOTIFICATION] Booking CONFIRMED: " + booking.getBookingId()
                + " | User " + booking.getUserId()
                + " | Movie: " + booking.getShow().getMovie().getTitle()
                + " | Seats: " + booking.getSeats().size()
                + " | Total: Rs." + booking.getTotalPrice());
    }

    @Override
    public void onBookingCancelled(Booking booking) {
        System.out.println("[NOTIFICATION] Booking CANCELLED: " + booking.getBookingId()
                + " | User " + booking.getUserId()
                + " | Movie: " + booking.getShow().getMovie().getTitle()
                + " | Refund: Rs." + booking.getTotalPrice());
    }
}

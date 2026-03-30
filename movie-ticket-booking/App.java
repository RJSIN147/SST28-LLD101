import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // 1. Create seats for a screen
        List<Seat> seats = new ArrayList<>();
        // Row 1: Silver (seats 1-3)
        seats.add(new Seat(1, SeatType.SILVER, 1));
        seats.add(new Seat(2, SeatType.SILVER, 1));
        seats.add(new Seat(3, SeatType.SILVER, 1));
        // Row 2: Gold (seats 4-6)
        seats.add(new Seat(4, SeatType.GOLD, 2));
        seats.add(new Seat(5, SeatType.GOLD, 2));
        seats.add(new Seat(6, SeatType.GOLD, 2));
        // Row 3: Platinum (seats 7-8)
        seats.add(new Seat(7, SeatType.PLATINUM, 3));
        seats.add(new Seat(8, SeatType.PLATINUM, 3));

        // 2. Create screen and theatre
        Screen screen1 = new Screen(1, seats);
        Theatre theatre = new Theatre(1, "PVR Cinemas");
        theatre.addScreen(screen1);

        // 3. Create a movie
        Movie movie = new Movie(1, "Inception", "Sci-Fi", 148);

        // 4. Create a show using the builder
        Show show = new ShowBuilder()
                .setShowId(1)
                .setMovie(movie)
                .setScreen(screen1)
                .setShowTime(LocalDateTime.of(2026, 3, 30, 18, 30))
                .build();
        theatre.addShow(show);

        // 5. Setup booking service with pricing and notifications
        BookingService bookingService = new BookingService(new StandardPricingStrategy());
        bookingService.addObserver(new ConsoleNotificationService());

        // 6. Display initial availability
        System.out.println("=== Initial Availability ===");
        show.displayAvailability();

        // 7. User 1 books 2 Gold seats (seats 4, 5)
        System.out.println("\n--- User 1: Booking 2 Gold Seats ---");
        Booking booking1 = bookingService.book(1, show, Arrays.asList(4, 5));

        // 8. User 2 books 1 Platinum seat (seat 7)
        System.out.println("\n--- User 2: Booking 1 Platinum Seat ---");
        Booking booking2 = bookingService.book(2, show, Arrays.asList(7));

        // 9. Display availability after bookings
        System.out.println("\n=== Availability After Bookings ===");
        show.displayAvailability();

        // 10. User 3 tries to book an already-booked seat (seat 4)
        System.out.println("\n--- User 3: Trying to Book Already-Booked Seat #4 ---");
        try {
            bookingService.book(3, show, Arrays.asList(4));
        } catch (IllegalStateException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        // 11. User 1 cancels their booking
        System.out.println("\n--- User 1: Cancelling Booking ---");
        bookingService.cancel(booking1.getBookingId());

        // 12. Display final availability (Gold seats 4, 5 should be available again)
        System.out.println("\n=== Final Availability (After Cancellation) ===");
        show.displayAvailability();

        // 13. User 3 can now book seat 4
        System.out.println("\n--- User 3: Booking Seat #4 (Now Available) ---");
        Booking booking3 = bookingService.book(3, show, Arrays.asList(4));

        System.out.println("\n=== Summary ===");
        System.out.println(booking1);
        System.out.println(booking2);
        System.out.println(booking3);
    }
}

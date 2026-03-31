import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) throws InterruptedException {
        // 1. Create seats for a screen
        List<Seat> seats = new ArrayList<>();
        seats.add(new Seat(1, SeatType.SILVER, 1));
        seats.add(new Seat(2, SeatType.SILVER, 1));
        seats.add(new Seat(3, SeatType.SILVER, 1));
        seats.add(new Seat(4, SeatType.GOLD, 2));
        seats.add(new Seat(5, SeatType.GOLD, 2));
        seats.add(new Seat(6, SeatType.GOLD, 2));
        seats.add(new Seat(7, SeatType.PLATINUM, 3));
        seats.add(new Seat(8, SeatType.PLATINUM, 3));

        Screen screen1 = new Screen(1, seats);
        Theatre theatre = new Theatre(1, "PVR Cinemas");
        theatre.addScreen(screen1);

        Movie movie = new Movie(1, "Inception", "Sci-Fi", 148);

        Show show = new ShowBuilder()
                .setShowId(1)
                .setMovie(movie)
                .setScreen(screen1)
                .setShowTime(LocalDateTime.of(2026, 3, 30, 18, 30))
                .build();
        theatre.addShow(show);

        // ============================================================
        // PART 1: Standard Pricing + Credit Card (single-threaded)
        // ============================================================
        System.out.println("========================================");
        System.out.println("  PART 1: Standard Pricing + Credit Card");
        System.out.println("========================================\n");

        BookingService service1 = new BookingService(
                new StandardPricingStrategy(), new CreditCardPayment());
        service1.addObserver(new ConsoleNotificationService());

        System.out.println("=== Initial Availability ===");
        show.displayAvailability();

        Booking b1 = service1.book(1, show, Arrays.asList(4, 5));
        System.out.println();
        Booking b2 = service1.book(2, show, Arrays.asList(7));

        // ============================================================
        // PART 2: Demand Pricing + UPI (single-threaded)
        // ============================================================
        System.out.println("\n========================================");
        System.out.println("  PART 2: Demand Pricing + UPI Payment");
        System.out.println("========================================\n");

        DemandPricingStrategy demandPricing = new DemandPricingStrategy(
                new StandardPricingStrategy(), 0.50, 1.5);
        demandPricing.setShow(show);

        BookingService service2 = new BookingService(demandPricing, new UPIPayment());
        service2.addObserver(new ConsoleNotificationService());

        Booking b3 = service2.book(3, show, Arrays.asList(1));
        System.out.println();
        Booking b4 = service2.book(4, show, Arrays.asList(6));

        // Cancel some bookings to free seats for concurrency test
        System.out.println("\n--- Resetting: Cancelling all bookings ---");
        service1.cancel(b1.getBookingId());
        service1.cancel(b2.getBookingId());
        service2.cancel(b3.getBookingId());
        service2.cancel(b4.getBookingId());

        System.out.println("\n=== All Seats Available Again ===");
        show.displayAvailability();

        // ============================================================
        // PART 3: CONCURRENCY TEST — 5 threads race for seat #4
        // ============================================================
        System.out.println("\n==========================================");
        System.out.println("  PART 3: CONCURRENCY TEST (5 threads)");
        System.out.println("==========================================\n");

        BookingService concurrentService = new BookingService(
                new StandardPricingStrategy(), new DebitCardPayment());
        concurrentService.addObserver(new ConsoleNotificationService());

        System.out.println("5 threads simultaneously trying to book Seat #4...\n");

        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 1; i <= 5; i++) {
            final int userId = i;
            executor.submit(() -> {
                try {
                    Booking b = concurrentService.book(userId, show, Arrays.asList(4));
                    System.out.println("  >>> User " + userId + " SUCCESS: " + b.getBookingId());
                } catch (Exception e) {
                    System.out.println("  >>> User " + userId + " FAILED: " + e.getMessage());
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("\n=== After Concurrency Test ===");
        show.displayAvailability();
        System.out.println("Expected: Exactly 1 SUCCESS, 4 FAILED");

        // ============================================================
        // PART 4: Multi-seat concurrent booking
        // ============================================================
        System.out.println("\n==============================================");
        System.out.println("  PART 4: CONCURRENT MULTI-SEAT BOOKING");
        System.out.println("==============================================\n");

        BookingService concurrentService2 = new BookingService(
                new StandardPricingStrategy(), new UPIPayment());
        concurrentService2.addObserver(new ConsoleNotificationService());

        System.out.println("3 threads: User A wants [1,2], User B wants [2,3], User C wants [5,6]\n");

        ExecutorService executor2 = Executors.newFixedThreadPool(3);

        executor2.submit(() -> {
            try {
                Booking b = concurrentService2.book(101, show, Arrays.asList(1, 2));
                System.out.println("  >>> User 101 SUCCESS: seats 1,2 | " + b.getBookingId());
            } catch (Exception e) {
                System.out.println("  >>> User 101 FAILED: " + e.getMessage());
            }
        });

        executor2.submit(() -> {
            try {
                Booking b = concurrentService2.book(102, show, Arrays.asList(2, 3));
                System.out.println("  >>> User 102 SUCCESS: seats 2,3 | " + b.getBookingId());
            } catch (Exception e) {
                System.out.println("  >>> User 102 FAILED: " + e.getMessage());
            }
        });

        executor2.submit(() -> {
            try {
                Booking b = concurrentService2.book(103, show, Arrays.asList(5, 6));
                System.out.println("  >>> User 103 SUCCESS: seats 5,6 | " + b.getBookingId());
            } catch (Exception e) {
                System.out.println("  >>> User 103 FAILED: " + e.getMessage());
            }
        });

        executor2.shutdown();
        executor2.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("\n=== Final Availability ===");
        show.displayAvailability();
        System.out.println("\nExpected: User 101 OR 102 gets seat 2 (not both). User 103 always succeeds (no overlap).");
    }
}

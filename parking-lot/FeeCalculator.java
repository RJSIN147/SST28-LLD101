import java.time.LocalDateTime;

public interface FeeCalculator {
    int calculateFee(Ticket ticket, LocalDateTime exitTime);
}

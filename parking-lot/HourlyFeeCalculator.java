import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class HourlyFeeCalculator implements FeeCalculator {
    private final Map<VehicleType, Integer> ratePerHour;

    public HourlyFeeCalculator() {
        ratePerHour = new HashMap<>();
        ratePerHour.put(VehicleType.MOTORCYCLE, 10);
        ratePerHour.put(VehicleType.CAR, 20);
        ratePerHour.put(VehicleType.TRUCK, 30);
    }

    public HourlyFeeCalculator(Map<VehicleType, Integer> customRates) {
        this.ratePerHour = new HashMap<>(customRates);
    }

    @Override
    public int calculateFee(Ticket ticket, LocalDateTime exitTime) {
        Duration duration = Duration.between(ticket.getEntryTime(), exitTime);
        long totalMinutes = duration.toMinutes();

        // Minimum charge of 1 hour; otherwise ceil to next hour
        long hours = Math.max(1, (long) Math.ceil(totalMinutes / 60.0));

        int rate = ratePerHour.getOrDefault(ticket.getVehicle().getType(), 20);
        return (int) (hours * rate);
    }
}

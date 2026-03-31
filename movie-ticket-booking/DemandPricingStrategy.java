public class DemandPricingStrategy implements PricingStrategy {
    private final PricingStrategy basePricingStrategy;
    private final double demandThreshold; // percentage of seats booked (0.0 to 1.0)
    private final double surgeMultiplier;
    private Show show;

    public DemandPricingStrategy(PricingStrategy basePricingStrategy,
                                  double demandThreshold, double surgeMultiplier) {
        this.basePricingStrategy = basePricingStrategy;
        this.demandThreshold = demandThreshold;
        this.surgeMultiplier = surgeMultiplier;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    @Override
    public int calculatePrice(SeatType seatType) {
        int basePrice = basePricingStrategy.calculatePrice(seatType);

        if (show == null) {
            return basePrice;
        }

        int totalSeats = show.getScreen().getSeats().size();
        int availableSeats = show.getAvailableSeats().size();
        double occupancyRate = 1.0 - ((double) availableSeats / totalSeats);

        if (occupancyRate >= demandThreshold) {
            int surgedPrice = (int) (basePrice * surgeMultiplier);
            System.out.println("  [SURGE PRICING] Occupancy at " + (int)(occupancyRate * 100)
                    + "% (threshold: " + (int)(demandThreshold * 100) + "%). "
                    + seatType + " price: " + basePrice + " -> " + surgedPrice);
            return surgedPrice;
        }

        return basePrice;
    }
}

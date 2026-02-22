public class StaffDiscountCalculator implements DiscountCalculator {
    @Override
    public double discountAmount(double subtotal, int distinctLines) {
        if (distinctLines >= 3)
            return 15.0;
        return 5.0;
    }
}

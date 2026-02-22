public class StudentDiscountCalculator implements DiscountCalculator {
    @Override
    public double discountAmount(double subtotal, int distinctLines) {
        if (subtotal >= 180.0)
            return 10.0;
        return 0.0;
    }
}

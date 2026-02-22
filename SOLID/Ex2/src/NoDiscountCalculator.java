public class NoDiscountCalculator implements DiscountCalculator {
    @Override
    public double discountAmount(double subtotal, int distinctLines) {
        return 0.0;
    }
}

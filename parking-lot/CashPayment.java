public class CashPayment implements PaymentGateway {

    @Override
    public boolean processPayment(int amount) {
        System.out.println("  [CASH] Received Rs." + amount + " in cash. Payment successful!");
        return true;
    }

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.CASH;
    }
}

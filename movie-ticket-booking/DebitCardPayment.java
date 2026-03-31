public class DebitCardPayment implements PaymentGateway {

    @Override
    public boolean processPayment(int userId, int amount) {
        // Simulate debit card payment processing
        System.out.println("  [DEBIT CARD] Processing Rs." + amount + " for User " + userId + "...");
        System.out.println("  [DEBIT CARD] Payment successful!");
        return true;
    }

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.DEBIT_CARD;
    }
}

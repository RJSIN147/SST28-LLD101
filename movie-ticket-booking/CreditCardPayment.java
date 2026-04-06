public class CreditCardPayment implements PaymentGateway {

    @Override
    public boolean processPayment(int userId, int amount) {
        System.out.println("  [CREDIT CARD] Processing Rs." + amount + " for User " + userId + "...");
        System.out.println("  [CREDIT CARD] Payment successful!");
        return true;
    }

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.CREDIT_CARD;
    }
}

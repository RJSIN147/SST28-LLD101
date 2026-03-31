public class DebitCardPayment implements PaymentGateway {

    @Override
    public boolean processPayment(int amount) {
        System.out.println("  [DEBIT CARD] Processing Rs." + amount + "...");
        System.out.println("  [DEBIT CARD] Payment successful!");
        return true;
    }

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.DEBIT_CARD;
    }
}

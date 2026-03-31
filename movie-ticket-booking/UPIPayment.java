public class UPIPayment implements PaymentGateway {

    @Override
    public boolean processPayment(int userId, int amount) {
        // Simulate UPI payment processing
        System.out.println("  [UPI] Processing Rs." + amount + " for User " + userId + "...");
        System.out.println("  [UPI] Payment successful!");
        return true;
    }

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.UPI;
    }
}

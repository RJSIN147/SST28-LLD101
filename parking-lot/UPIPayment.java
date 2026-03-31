public class UPIPayment implements PaymentGateway {

    @Override
    public boolean processPayment(int amount) {
        System.out.println("  [UPI] Processing Rs." + amount + "...");
        System.out.println("  [UPI] Payment successful!");
        return true;
    }

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.UPI;
    }
}

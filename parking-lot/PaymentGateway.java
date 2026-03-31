public interface PaymentGateway {
    boolean processPayment(int amount);
    PaymentMethod getPaymentMethod();
}

public interface PaymentGateway {
    boolean processPayment(int userId, int amount);
    PaymentMethod getPaymentMethod();
}

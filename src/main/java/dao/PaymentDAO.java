package dao;

import model.Payment;
import java.util.List;

public interface PaymentDAO {
    void addPayment(Payment payment);
    Payment getPaymentById(int id);
    List<Payment> getPaymentsByOrderId(int orderId);
    List<Payment> getAllPayments();
    void updatePayment(Payment payment);
    void deletePayment(int id);
}
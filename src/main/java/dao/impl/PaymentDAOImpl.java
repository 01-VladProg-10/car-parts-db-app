package dao.impl;

import dao.PaymentDAO;
import lombok.extern.slf4j.Slf4j;
import model.Order;
import model.Payment;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PaymentDAOImpl implements PaymentDAO {

    private final Connection connection;

    public PaymentDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addPayment(Payment payment) {
        String sql = "INSERT INTO payments (order_id, amount, payment_date, method) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, payment.getOrder().getId());
            stmt.setBigDecimal(2, payment.getAmount());
            stmt.setTimestamp(3, Timestamp.valueOf(payment.getPaymentDate()));
            stmt.setString(4, payment.getMethod());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    payment.setId(rs.getInt(1));
                }
            }

            log.info("Payment added: id={} for order={}", payment.getId(), payment.getOrder().getId());

        } catch (SQLException e) {
            log.error("Error adding payment", e);
        }
    }

    @Override
    public Payment getPaymentById(int id) {
        String sql = "SELECT id, order_id, amount, payment_date, method FROM payments WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("order_id"));

                return new Payment(
                        rs.getInt("id"),
                        order,
                        rs.getBigDecimal("amount"),
                        rs.getTimestamp("payment_date").toLocalDateTime(),
                        rs.getString("method")
                );
            }

        } catch (SQLException e) {
            log.error("Error fetching payment by id: {}", id, e);
        }

        return null;
    }

    @Override
    public List<Payment> getPaymentsByOrderId(int orderId) {
        List<Payment> payments = new ArrayList<>();

        String sql = "SELECT id, amount, payment_date, method FROM payments WHERE order_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setId(orderId);

                payments.add(new Payment(
                        rs.getInt("id"),
                        order,
                        rs.getBigDecimal("amount"),
                        rs.getTimestamp("payment_date").toLocalDateTime(),
                        rs.getString("method")
                ));
            }

        } catch (SQLException e) {
            log.error("Error fetching payments for order: {}", orderId, e);
        }

        return payments;
    }

    @Override
    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();

        String sql = "SELECT id, order_id, amount, payment_date, method FROM payments";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("order_id"));

                payments.add(new Payment(
                        rs.getInt("id"),
                        order,
                        rs.getBigDecimal("amount"),
                        rs.getTimestamp("payment_date").toLocalDateTime(),
                        rs.getString("method")
                ));
            }

        } catch (SQLException e) {
            log.error("Error fetching all payments", e);
        }

        return payments;
    }

    @Override
    public void updatePayment(Payment payment) {
        String sql = "UPDATE payments SET amount = ?, payment_date = ?, method = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, payment.getAmount());
            stmt.setTimestamp(2, Timestamp.valueOf(payment.getPaymentDate()));
            stmt.setString(3, payment.getMethod());
            stmt.setInt(4, payment.getId());

            int updated = stmt.executeUpdate();

            if (updated > 0) {
                log.info("Payment updated: {}", payment.getId());
            } else {
                log.warn("No payment found with id: {}", payment.getId());
            }

        } catch (SQLException e) {
            log.error("Error updating payment: {}", payment.getId(), e);
        }
    }

    @Override
    public void deletePayment(int id) {
        String sql = "DELETE FROM payments WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int deleted = stmt.executeUpdate();

            if (deleted > 0) {
                log.info("Payment deleted: {}", id);
            } else {
                log.warn("No payment found with id: {}", id);
            }

        } catch (SQLException e) {
            log.error("Error deleting payment: {}", id, e);
        }
    }
}
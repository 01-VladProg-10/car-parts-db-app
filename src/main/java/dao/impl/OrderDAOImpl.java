package dao.impl;

import dao.OrderDAO;
import lombok.extern.slf4j.Slf4j;
import model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrderDAOImpl implements OrderDAO {

    private final Connection connection;

    public OrderDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addOrder(Order order) {
        String sql = "INSERT INTO orders (user_id, order_date, status_id) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, order.getUser().getId());
            stmt.setTimestamp(2, Timestamp.valueOf(order.getOrderDate()));
            stmt.setInt(3, order.getStatus().getId());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    order.setId(rs.getInt(1));
                }
            }

            log.info("Order added: id={} for userId={}", order.getId(), order.getUser().getId());

        } catch (SQLException e) {
            log.error("Error adding Order for userId={}", order.getUser().getId(), e);
        }
    }

    @Override
    public Order getOrderById(int id) {
        String sql =
                "SELECT o.*, " +
                        "u.id as user_id, u.name, u.email, " +
                        "s.id as status_id, s.name as status_name " +
                        "FROM orders o " +
                        "JOIN users u ON o.user_id = u.id " +
                        "JOIN order_status s ON o.status_id = s.id " +
                        "WHERE o.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        null,      // password
                        null
                );

                OrderStatus status = new OrderStatus(
                        rs.getInt("status_id"),
                        rs.getString("status_name")
                );

                Order order = new Order(
                        rs.getInt("id"),
                        user,
                        rs.getTimestamp("order_date").toLocalDateTime(),
                        status,
                        new ArrayList<>() // items ładowane osobno
                );

                log.info("Order fetched by id={}", id);
                return order;
            }

        } catch (SQLException e) {
            log.error("Error fetching Order id={}", id, e);
        }

        return null;
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();

        String sql =
                "SELECT o.*, " +
                        "u.id as user_id, u.username, u.email, " +
                        "s.id as status_id, s.name as status_name " +
                        "FROM orders o " +
                        "JOIN users u ON o.user_id = u.id " +
                        "JOIN order_status s ON o.status_id = s.id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        null,      // password
                        null       // role (jeśli nie joinujesz ról)
                );

                OrderStatus status = new OrderStatus(
                        rs.getInt("status_id"),
                        rs.getString("status_name")
                );

                orders.add(new Order(
                        rs.getInt("id"),
                        user,
                        rs.getTimestamp("order_date").toLocalDateTime(),
                        status,
                        new ArrayList<>()
                ));
            }

            log.info("Fetched all Orders, count={}", orders.size());

        } catch (SQLException e) {
            log.error("Error fetching all Orders", e);
        }

        return orders;
    }

    @Override
    public void updateOrderStatus(int orderId, int statusId) {
        String sql = "UPDATE orders SET status_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, statusId);
            stmt.setInt(2, orderId);

            int updated = stmt.executeUpdate();
            if (updated > 0) {
                log.info("Order status updated for orderId={}", orderId);
            } else {
                log.warn("No Order found for id={}", orderId);
            }
        } catch (SQLException e) {
            log.error("Error updating Order status id={}", orderId, e);
        }
    }

    @Override
    public void deleteOrder(int id) {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int deleted = stmt.executeUpdate();

            if (deleted > 0) {
                log.info("Order deleted id={}", id);
            } else {
                log.warn("No Order found to delete id={}", id);
            }
        } catch (SQLException e) {
            log.error("Error deleting Order id={}", id, e);
        }
    }
}
package dao.impl;

import dao.OrderItemDAO;
import lombok.extern.slf4j.Slf4j;
import model.Order;
import model.OrderItem;
import model.Product;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrderItemDAOImpl implements OrderItemDAO {

    private final Connection connection;

    public OrderItemDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addOrderItem(OrderItem orderItem) {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, orderItem.getOrder().getId());
            stmt.setInt(2, orderItem.getProduct().getId());
            stmt.setInt(3, orderItem.getQuantity());
            stmt.setBigDecimal(4, orderItem.getPrice());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    orderItem.setId(rs.getInt(1));
                }
            }

            log.info("OrderItem added: id={} for order={}", orderItem.getId(), orderItem.getOrder().getId());

        } catch (SQLException e) {
            log.error("Error adding OrderItem", e);
        }
    }

    @Override
    public OrderItem getOrderItemById(int id) {
        String sql = """
                SELECT oi.id, oi.quantity, oi.price,
                       o.id as order_id,
                       p.id as product_id
                FROM order_items oi
                JOIN orders o ON oi.order_id = o.id
                JOIN products p ON oi.product_id = p.id
                WHERE oi.id = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("order_id"));

                Product product = new Product();
                product.setId(rs.getInt("product_id"));

                return new OrderItem(
                        rs.getInt("id"),
                        order,
                        product,
                        rs.getInt("quantity"),
                        rs.getBigDecimal("price")
                );
            }
        } catch (SQLException e) {
            log.error("Error fetching OrderItem by id: {}", id, e);
        }

        return null;
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> items = new ArrayList<>();

        String sql = """
                SELECT id, product_id, quantity, price
                FROM order_items
                WHERE order_id = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setId(orderId);

                Product product = new Product();
                product.setId(rs.getInt("product_id"));

                items.add(new OrderItem(
                        rs.getInt("id"),
                        order,
                        product,
                        rs.getInt("quantity"),
                        rs.getBigDecimal("price")
                ));
            }

        } catch (SQLException e) {
            log.error("Error fetching OrderItems for order: {}", orderId, e);
        }

        return items;
    }

    @Override
    public List<OrderItem> getAllOrderItems() {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT id, order_id, product_id, quantity, price FROM order_items";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("order_id"));

                Product product = new Product();
                product.setId(rs.getInt("product_id"));

                items.add(new OrderItem(
                        rs.getInt("id"),
                        order,
                        product,
                        rs.getInt("quantity"),
                        rs.getBigDecimal("price")
                ));
            }

        } catch (SQLException e) {
            log.error("Error fetching all OrderItems", e);
        }

        return items;
    }

    @Override
    public void updateOrderItem(OrderItem orderItem) {
        String sql = "UPDATE order_items SET quantity = ?, price = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderItem.getQuantity());
            stmt.setBigDecimal(2, orderItem.getPrice());
            stmt.setInt(3, orderItem.getId());

            int updated = stmt.executeUpdate();
            if (updated > 0) {
                log.info("OrderItem updated: {}", orderItem.getId());
            } else {
                log.warn("No OrderItem found with id: {}", orderItem.getId());
            }

        } catch (SQLException e) {
            log.error("Error updating OrderItem: {}", orderItem.getId(), e);
        }
    }

    @Override
    public void deleteOrderItem(int id) {
        String sql = "DELETE FROM order_items WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int deleted = stmt.executeUpdate();

            if (deleted > 0) {
                log.info("OrderItem deleted: {}", id);
            } else {
                log.warn("No OrderItem found with id: {}", id);
            }

        } catch (SQLException e) {
            log.error("Error deleting OrderItem: {}", id, e);
        }
    }
}
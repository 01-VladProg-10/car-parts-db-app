package dao.impl;

import dao.OrderStatusDAO;
import lombok.extern.slf4j.Slf4j;
import model.OrderStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrderStatusDAOImpl implements OrderStatusDAO {

    private final Connection connection;

    public OrderStatusDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addOrderStatus(OrderStatus status) {
        String sql = "INSERT INTO order_status (name) VALUES (?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, status.getName());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    status.setId(rs.getInt(1));
                }
            }

            log.info("OrderStatus added: {} with id={}", status.getName(), status.getId());

        } catch (SQLException e) {
            log.error("Error adding OrderStatus: {}", status.getName(), e);
        }
    }

    @Override
    public OrderStatus getOrderStatusById(int id) {
        String sql = "SELECT * FROM order_status WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                OrderStatus status = new OrderStatus(rs.getInt("id"), rs.getString("name"));
                log.info("OrderStatus fetched by id: {}", id);
                return status;
            }
        } catch (SQLException e) {
            log.error("Error fetching OrderStatus by id: {}", id, e);
        }
        return null;
    }

    @Override
    public List<OrderStatus> getAllOrderStatuses() {
        List<OrderStatus> statuses = new ArrayList<>();
        String sql = "SELECT * FROM order_status";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                statuses.add(new OrderStatus(rs.getInt("id"), rs.getString("name")));
            }
            log.info("Fetched all OrderStatuses, count: {}", statuses.size());
        } catch (SQLException e) {
            log.error("Error fetching all OrderStatuses", e);
        }
        return statuses;
    }

    @Override
    public void updateOrderStatus(OrderStatus status) {
        String sql = "UPDATE order_status SET name = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.getName());
            stmt.setInt(2, status.getId());
            int updated = stmt.executeUpdate();
            if (updated > 0) {
                log.info("OrderStatus updated: {}", status.getId());
            } else {
                log.warn("No OrderStatus found to update with id: {}", status.getId());
            }
        } catch (SQLException e) {
            log.error("Error updating OrderStatus: {}", status.getId(), e);
        }
    }

    @Override
    public void deleteOrderStatus(int id) {
        String sql = "DELETE FROM order_status WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int deleted = stmt.executeUpdate();
            if (deleted > 0) {
                log.info("OrderStatus deleted: {}", id);
            } else {
                log.warn("No OrderStatus found to delete with id: {}", id);
            }
        } catch (SQLException e) {
            log.error("Error deleting OrderStatus: {}", id, e);
        }
    }
}
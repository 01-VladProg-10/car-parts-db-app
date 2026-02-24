package dao.impl;

import dao.ShipmentDAO;
import lombok.extern.slf4j.Slf4j;
import model.Order;
import model.Shipment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ShipmentDAOImpl implements ShipmentDAO {

    private final Connection connection;

    public ShipmentDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addShipment(Shipment shipment) {
        String sql = "INSERT INTO shipments (order_id, shipped_date, delivery_date, tracking_number) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, shipment.getOrder().getId());
            stmt.setTimestamp(2, Timestamp.valueOf(shipment.getShippedDate()));

            if (shipment.getDeliveryDate() != null) {
                stmt.setTimestamp(3, Timestamp.valueOf(shipment.getDeliveryDate()));
            } else {
                stmt.setNull(3, Types.TIMESTAMP);
            }

            stmt.setString(4, shipment.getTrackingNumber());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    shipment.setId(rs.getInt(1));
                }
            }

            log.info("Shipment added: id={} for orderId={}", shipment.getId(), shipment.getOrder().getId());

        } catch (SQLException e) {
            log.error("Error adding Shipment for orderId={}", shipment.getOrder().getId(), e);
        }
    }

    @Override
    public Shipment getShipmentById(int id) {
        String sql = "SELECT * FROM shipments WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Order order = new Order();
                order.setId(rs.getInt("order_id"));

                Shipment shipment = new Shipment(
                        rs.getInt("id"),
                        order,
                        rs.getTimestamp("shipped_date").toLocalDateTime(),
                        rs.getTimestamp("delivery_date") != null
                                ? rs.getTimestamp("delivery_date").toLocalDateTime()
                                : null,
                        rs.getString("tracking_number")
                );

                log.info("Shipment fetched by id={}", id);
                return shipment;
            }

        } catch (SQLException e) {
            log.error("Error fetching Shipment id={}", id, e);
        }

        return null;
    }

    @Override
    public Shipment getShipmentByOrderId(int orderId) {
        String sql = "SELECT * FROM shipments WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Order order = new Order();
                order.setId(orderId);

                Shipment shipment = new Shipment(
                        rs.getInt("id"),
                        order,
                        rs.getTimestamp("shipped_date").toLocalDateTime(),
                        rs.getTimestamp("delivery_date") != null
                                ? rs.getTimestamp("delivery_date").toLocalDateTime()
                                : null,
                        rs.getString("tracking_number")
                );

                log.info("Shipment fetched for orderId={}", orderId);
                return shipment;
            }

        } catch (SQLException e) {
            log.error("Error fetching Shipment for orderId={}", orderId, e);
        }

        return null;
    }

    @Override
    public List<Shipment> getAllShipments() {
        List<Shipment> shipments = new ArrayList<>();
        String sql = "SELECT * FROM shipments";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                Order order = new Order();
                order.setId(rs.getInt("order_id"));

                shipments.add(new Shipment(
                        rs.getInt("id"),
                        order,
                        rs.getTimestamp("shipped_date").toLocalDateTime(),
                        rs.getTimestamp("delivery_date") != null
                                ? rs.getTimestamp("delivery_date").toLocalDateTime()
                                : null,
                        rs.getString("tracking_number")
                ));
            }

            log.info("Fetched all Shipments, count={}", shipments.size());

        } catch (SQLException e) {
            log.error("Error fetching all Shipments", e);
        }

        return shipments;
    }

    @Override
    public void updateShipment(Shipment shipment) {
        String sql = "UPDATE shipments SET shipped_date = ?, delivery_date = ?, tracking_number = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(shipment.getShippedDate()));

            if (shipment.getDeliveryDate() != null) {
                stmt.setTimestamp(2, Timestamp.valueOf(shipment.getDeliveryDate()));
            } else {
                stmt.setNull(2, Types.TIMESTAMP);
            }

            stmt.setString(3, shipment.getTrackingNumber());
            stmt.setInt(4, shipment.getId());

            int updated = stmt.executeUpdate();
            if (updated > 0) {
                log.info("Shipment updated id={}", shipment.getId());
            } else {
                log.warn("No Shipment found to update id={}", shipment.getId());
            }

        } catch (SQLException e) {
            log.error("Error updating Shipment id={}", shipment.getId(), e);
        }
    }

    @Override
    public void deleteShipment(int id) {
        String sql = "DELETE FROM shipments WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int deleted = stmt.executeUpdate();

            if (deleted > 0) {
                log.info("Shipment deleted id={}", id);
            } else {
                log.warn("No Shipment found to delete id={}", id);
            }

        } catch (SQLException e) {
            log.error("Error deleting Shipment id={}", id, e);
        }
    }
}
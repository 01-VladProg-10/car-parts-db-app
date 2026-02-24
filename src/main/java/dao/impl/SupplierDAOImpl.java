package dao.impl;

import dao.SupplierDAO;
import lombok.extern.slf4j.Slf4j;
import model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SupplierDAOImpl implements SupplierDAO {

    private final Connection connection;

    public SupplierDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addSupplier(Supplier supplier) {
        String sql = "INSERT INTO suppliers (name, contact_email, phone, address) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getContactEmail());
            stmt.setString(3, supplier.getPhone());
            stmt.setString(4, supplier.getAddress());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    supplier.setId(rs.getInt(1));
                }
            }

            log.info("Supplier added: {} with id={}", supplier.getName(), supplier.getId());

        } catch (SQLException e) {
            log.error("Error adding Supplier: {}", supplier.getName(), e);
        }
    }

    @Override
    public Supplier getSupplierById(int id) {
        String sql = "SELECT * FROM suppliers WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Supplier supplier = new Supplier(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("contact_email"),
                        rs.getString("phone"),
                        rs.getString("address")
                );
                log.info("Supplier fetched by id: {}", id);
                return supplier;
            }
        } catch (SQLException e) {
            log.error("Error fetching Supplier by id: {}", id, e);
        }
        return null;
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                suppliers.add(new Supplier(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("contact_email"),
                        rs.getString("phone"),
                        rs.getString("address")
                ));
            }
            log.info("Fetched all Suppliers, count: {}", suppliers.size());
        } catch (SQLException e) {
            log.error("Error fetching all Suppliers", e);
        }
        return suppliers;
    }

    @Override
    public void updateSupplier(Supplier supplier) {
        String sql = "UPDATE suppliers SET name = ?, contact_email = ?, phone = ?, address = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getContactEmail());
            stmt.setString(3, supplier.getPhone());
            stmt.setString(4, supplier.getAddress());
            stmt.setInt(5, supplier.getId());

            int updated = stmt.executeUpdate();
            if (updated > 0) {
                log.info("Supplier updated: {}", supplier.getId());
            } else {
                log.warn("No Supplier found to update with id: {}", supplier.getId());
            }
        } catch (SQLException e) {
            log.error("Error updating Supplier: {}", supplier.getId(), e);
        }
    }

    @Override
    public void deleteSupplier(int id) {
        String sql = "DELETE FROM suppliers WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int deleted = stmt.executeUpdate();
            if (deleted > 0) {
                log.info("Supplier deleted: {}", id);
            } else {
                log.warn("No Supplier found to delete with id: {}", id);
            }
        } catch (SQLException e) {
            log.error("Error deleting Supplier: {}", id, e);
        }
    }
}
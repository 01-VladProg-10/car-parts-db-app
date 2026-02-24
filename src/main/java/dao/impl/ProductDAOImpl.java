package dao.impl;

import dao.ProductDAO;
import lombok.extern.slf4j.Slf4j;
import model.Category;
import model.Product;
import model.Supplier;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProductDAOImpl implements ProductDAO {

    private final Connection connection;

    public ProductDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addProduct(Product product) {
        String sql = "INSERT INTO products (name, description, price, stock, category_id, supplier_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setInt(4, product.getStock());
            stmt.setInt(5, product.getCategory().getId());
            stmt.setInt(6, product.getSupplier().getId());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    product.setId(rs.getInt(1));
                }
            }

            log.info("Product added: {} with id={}", product.getName(), product.getId());

        } catch (SQLException e) {
            log.error("Error adding Product: {}", product.getName(), e);
        }
    }

    @Override
    public Product getProductById(int id) {
        String sql = "SELECT p.*, c.id as category_id, c.name as category_name, s.id as supplier_id, s.name as supplier_name, s.contact_email, s.phone, s.address " +
                "FROM products p " +
                "JOIN categories c ON p.category_id = c.id " +
                "JOIN suppliers s ON p.supplier_id = s.id " +
                "WHERE p.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Category category = new Category(rs.getInt("category_id"), rs.getString("category_name"));
                Supplier supplier = new Supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("contact_email"),
                        rs.getString("phone"),
                        rs.getString("address")
                );
                Product product = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock"),
                        category,
                        supplier
                );
                log.info("Product fetched by id: {}", id);
                return product;
            }
        } catch (SQLException e) {
            log.error("Error fetching Product by id: {}", id, e);
        }
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.id as category_id, c.name as category_name, s.id as supplier_id, s.name as supplier_name, s.contact_email, s.phone, s.address " +
                "FROM products p " +
                "JOIN categories c ON p.category_id = c.id " +
                "JOIN suppliers s ON p.supplier_id = s.id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Category category = new Category(rs.getInt("category_id"), rs.getString("category_name"));
                Supplier supplier = new Supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("contact_email"),
                        rs.getString("phone"),
                        rs.getString("address")
                );
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock"),
                        category,
                        supplier
                ));
            }
            log.info("Fetched all Products, count: {}", products.size());
        } catch (SQLException e) {
            log.error("Error fetching all Products", e);
        }
        return products;
    }

    @Override
    public void updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock = ?, category_id = ?, supplier_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setInt(4, product.getStock());
            stmt.setInt(5, product.getCategory().getId());
            stmt.setInt(6, product.getSupplier().getId());
            stmt.setInt(7, product.getId());

            int updated = stmt.executeUpdate();
            if (updated > 0) {
                log.info("Product updated: {}", product.getId());
            } else {
                log.warn("No Product found to update with id: {}", product.getId());
            }
        } catch (SQLException e) {
            log.error("Error updating Product: {}", product.getId(), e);
        }
    }

    @Override
    public void deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int deleted = stmt.executeUpdate();
            if (deleted > 0) {
                log.info("Product deleted: {}", id);
            } else {
                log.warn("No Product found to delete with id: {}", id);
            }
        } catch (SQLException e) {
            log.error("Error deleting Product: {}", id, e);
        }
    }
}
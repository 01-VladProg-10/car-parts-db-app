package dao.impl;

import dao.CategoryDAO;
import lombok.extern.slf4j.Slf4j;
import model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CategoryDAOImpl implements CategoryDAO {

    private final Connection connection;

    public CategoryDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addCategory(Category category) {
        String sql = "INSERT INTO categories (name) VALUES (?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, category.getName());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    category.setId(rs.getInt(1));
                }
            }

            log.info("Category added: {} with id={}", category.getName(), category.getId());

        } catch (SQLException e) {
            log.error("Error adding Category: {}", category.getName(), e);
        }
    }

    @Override
    public Category getCategoryById(int id) {
        String sql = "SELECT * FROM categories WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Category category = new Category(rs.getInt("id"), rs.getString("name"));
                log.info("Category fetched by id: {}", id);
                return category;
            }
        } catch (SQLException e) {
            log.error("Error fetching Category by id: {}", id, e);
        }
        return null;
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categories.add(new Category(rs.getInt("id"), rs.getString("name")));
            }
            log.info("Fetched all Categories, count: {}", categories.size());
        } catch (SQLException e) {
            log.error("Error fetching all Categories", e);
        }
        return categories;
    }

    @Override
    public void updateCategory(Category category) {
        String sql = "UPDATE categories SET name = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, category.getName());
            stmt.setInt(2, category.getId());
            int updated = stmt.executeUpdate();
            if (updated > 0) {
                log.info("Category updated: {}", category.getId());
            } else {
                log.warn("No Category found to update with id: {}", category.getId());
            }
        } catch (SQLException e) {
            log.error("Error updating Category: {}", category.getId(), e);
        }
    }

    @Override
    public void deleteCategory(int id) {
        String sql = "DELETE FROM categories WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int deleted = stmt.executeUpdate();
            if (deleted > 0) {
                log.info("Category deleted: {}", id);
            } else {
                log.warn("No Category found to delete with id: {}", id);
            }
        } catch (SQLException e) {
            log.error("Error deleting Category: {}", id, e);
        }
    }
}
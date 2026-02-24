package dao.impl;

import dao.UserDAO;
import model.User;
import model.Role;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserDAOImpl implements UserDAO {

    private final Connection connection;

    public UserDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addUser(User user) {
        String sql = "INSERT INTO users (name, email, password, role_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setInt(4, user.getRole().getId());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            }

            log.info("User added: {} with id={}", user.getName(), user.getId());

        } catch (SQLException e) {
            log.error("Error adding user: {}", user.getName(), e);
        }
    }

    @Override
    public User getUserById(int id) {
        String sql = "SELECT u.id, u.name, u.email, u.password, r.id as role_id, r.name as role_name " +
                "FROM users u JOIN roles r ON u.role_id = r.id WHERE u.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Role role = new Role(rs.getInt("role_id"), rs.getString("role_name"));
                User user = new User(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        role);
                log.info("User fetched by id: {}", id);
                return user;
            }
        } catch (SQLException e) {
            log.error("Error fetching user by id: {}", id, e);
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.id, u.name, u.email, u.password, r.id as role_id, r.name as role_name " +
                "FROM users u JOIN roles r ON u.role_id = r.id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Role role = new Role(rs.getInt("role_id"), rs.getString("role_name"));
                users.add(new User(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        role));
            }
            log.info("Fetched all users, count: {}", users.size());
        } catch (SQLException e) {
            log.error("Error fetching all users", e);
        }
        return users;
    }

    @Override
    public void updateUser(User user) {
        String sql = "UPDATE users SET name = ?, email = ?, password = ?, role_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setInt(4, user.getRole().getId());
            stmt.setInt(5, user.getId());
            int updated = stmt.executeUpdate();
            if (updated > 0) {
                log.info("User updated: {}", user.getId());
            } else {
                log.warn("No user found to update with id: {}", user.getId());
            }
        } catch (SQLException e) {
            log.error("Error updating user: {}", user.getId(), e);
        }
    }

    @Override
    public void deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int deleted = stmt.executeUpdate();
            if (deleted > 0) {
                log.info("User deleted: {}", id);
            } else {
                log.warn("No user found to delete with id: {}", id);
            }
        } catch (SQLException e) {
            log.error("Error deleting user: {}", id, e);
        }
    }
}
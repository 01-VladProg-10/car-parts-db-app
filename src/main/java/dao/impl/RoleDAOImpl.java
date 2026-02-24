package dao.impl;

import dao.RoleDAO;
import model.Role;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RoleDAOImpl implements RoleDAO {

    private final Connection connection;

    public RoleDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addRole(Role role) {
        String sql = "INSERT INTO roles (name) VALUES (?)";

        try (PreparedStatement stmt = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, role.getName());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    role.setId(rs.getInt(1));
                }
            }

            log.info("Role added: {} with id={}", role.getName(), role.getId());

        } catch (SQLException e) {
            log.error("Error adding role: {}", role.getName(), e);
        }
    }

    @Override
    public Role getRoleById(int id) {
        String sql = "SELECT * FROM roles WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Role(
                            rs.getInt("id"),
                            rs.getString("name")
                    );
                }
            }

        } catch (SQLException e) {
            log.error("Error fetching role by id: {}", id, e);
        }

        return null;
    }

    @Override
    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM roles";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                roles.add(new Role(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }

        } catch (SQLException e) {
            log.error("Error fetching all roles", e);
        }

        return roles;
    }

    @Override
    public void updateRole(Role role) {
        String sql = "UPDATE roles SET name = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, role.getName());
            stmt.setInt(2, role.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            log.error("Error updating role: {}", role.getId(), e);
        }
    }

    @Override
    public void deleteRole(int id) {
        String sql = "DELETE FROM roles WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id); // 🔥 BRAKOWAŁO TEGO
            stmt.executeUpdate();

        } catch (SQLException e) {
            log.error("Error deleting role: {}", id, e);
        }
    }
}
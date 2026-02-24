package dao;

import model.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface RoleDAO {
    void addRole(Role role);
    Role getRoleById(int id);
    List<Role> getAllRoles();
    void updateRole(Role role);
    void deleteRole(int id);
}
package com.example.MualaFuel_Backend.dao;

import com.example.MualaFuel_Backend.entity.Role;
import com.example.MualaFuel_Backend.factory.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoleDao {

    public void save(Role role) {
        final String SQL = "INSERT INTO role (id, name) VALUES (?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {
            stmt.setLong(1, role.getId());
            stmt.setString(2, role.getName());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Optional<Role> findById(Long id) {
        final String SQL = "SELECT id, name FROM role WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Role role = new Role();
                    role.setId(rs.getLong("id"));
                    role.setName(rs.getString("name"));
                    return Optional.of(role);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return Optional.empty();
    }

    public Optional<Role> findByName(String name) {
        final String SQL = "SELECT id, name FROM role WHERE name = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Role role = new Role();
                    role.setId(rs.getLong("id"));
                    role.setName(rs.getString("name"));
                    return Optional.of(role);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return Optional.empty();
    }

    public List<Role> findAll() {
        final String SQL = "SELECT id, name FROM role";
        List<Role> roles = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getLong("id"));
                role.setName(rs.getString("name"));
                roles.add(role);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return roles;
    }

    public void update(Role role) {
        final String SQL = "UPDATE role SET name = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {
            stmt.setString(1, role.getName());
            stmt.setLong(2, role.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void delete(Long id) {
        final String SQL = "DELETE FROM role WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}

package com.example.MualaFuel_Backend.dao;

import com.example.MualaFuel_Backend.entity.Role;
import com.example.MualaFuel_Backend.entity.User;
import com.example.MualaFuel_Backend.factory.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class UserDao {

    public User save(User user) {
        final String SQL_USER = "INSERT INTO \"user\" (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";
        final String SQL_USER_ROLE = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement statement = conn.prepareStatement(SQL_USER, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, user.getFirstName());
                statement.setString(2, user.getLastName());
                statement.setString(3, user.getEmail());
                statement.setString(4, user.getPassword());
                statement.executeUpdate();

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            }

            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                try (PreparedStatement statement = conn.prepareStatement(SQL_USER_ROLE)) {
                    for (Role role : user.getRoles()) {
                        statement.setLong(1, user.getId());
                        statement.setLong(2, role.getId());
                        statement.addBatch();
                    }
                    statement.executeBatch();
                }
            }

            conn.commit();
            return user;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Optional<User> findById(long id) {
        final String SQL_USER = "SELECT id, first_name, last_name, email, password FROM \"user\" WHERE id = ?";
        final String SQL_ROLES = "SELECT r.id, r.name FROM role r " +
                "JOIN user_role ur ON r.id = ur.role_id " +
                "WHERE ur.user_id = ?";

        try (Connection conn = ConnectionFactory.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(SQL_USER);
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));

                    try (PreparedStatement roleStmt = conn.prepareStatement(SQL_ROLES)) {
                        roleStmt.setLong(1, user.getId());
                        try (ResultSet roleRs = roleStmt.executeQuery()) {
                            Set<Role> roles = new HashSet<>();
                            while (roleRs.next()) {
                                Role role = new Role();
                                role.setId(roleRs.getLong("id"));
                                role.setName(roleRs.getString("name"));
                                roles.add(role);
                            }
                            user.setRoles(roles);
                        }
                    }

                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public Optional<User> findByEmail(String email) {
        final String SQL_USER = "SELECT id, first_name, last_name, email, password FROM \"user\" WHERE email = ?";
        final String SQL_ROLES = "SELECT r.id, r.name FROM role r " +
                "JOIN user_role ur ON r.id = ur.role_id " +
                "WHERE ur.user_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement userStmt = conn.prepareStatement(SQL_USER)) {

            userStmt.setString(1, email);
            try (ResultSet rs = userStmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));

                    try (PreparedStatement roleStmt = conn.prepareStatement(SQL_ROLES)) {
                        roleStmt.setLong(1, user.getId());
                        try (ResultSet roleRs = roleStmt.executeQuery()) {
                            Set<Role> roles = new HashSet<>();
                            while (roleRs.next()) {
                                Role role = new Role();
                                role.setId(roleRs.getLong("id"));
                                role.setName(roleRs.getString("name"));
                                roles.add(role);
                            }
                            user.setRoles(roles);
                        }
                    }

                    return Optional.of(user);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}

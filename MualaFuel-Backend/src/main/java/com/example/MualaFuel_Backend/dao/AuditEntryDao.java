package com.example.MualaFuel_Backend.dao;

import com.example.MualaFuel_Backend.entity.AuditEntry;
import com.example.MualaFuel_Backend.factory.ConnectionFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

@Repository
public class AuditEntryDao {
    private static final String SQL =
            "INSERT INTO audit_entry (level, event_type, source, details, created_at) VALUES (?, ?, ?, ?, ?)";

    public void save(AuditEntry e) {
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(SQL)) {
            c.setAutoCommit(false);
            ps.setString(1, e.getLevel().name());
            ps.setString(2, e.getEventType());
            ps.setString(3, e.getSource());
            ps.setString(4, e.getDetails());
            ps.setTimestamp(5, Timestamp.valueOf(e.getCreatedAt()));
            ps.executeUpdate();
            c.commit();
        } catch (SQLException ex) {
            throw new RuntimeException("Cannot save audit entry", ex);
        }
    }
}


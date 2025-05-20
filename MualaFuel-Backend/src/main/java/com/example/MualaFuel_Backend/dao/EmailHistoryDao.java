package com.example.MualaFuel_Backend.dao;

import com.example.MualaFuel_Backend.dto.request.EmailFilterRequest;
import com.example.MualaFuel_Backend.entity.EmailHistory;
import com.example.MualaFuel_Backend.entity.Order;
import com.example.MualaFuel_Backend.factory.ConnectionFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EmailHistoryDao {
    public EmailHistory save(EmailHistory emailHistory) throws SQLException {
        String insert = "INSERT INTO email_history (recipient, subject, body, sent_at, related_order_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, emailHistory.getRecipient());
            ps.setString(2, emailHistory.getSubject());
            ps.setString(3, emailHistory.getBody());
            ps.setTimestamp(4, Timestamp.valueOf(emailHistory.getSentAt()));
            if (emailHistory.getOrder() != null) ps.setLong(5, emailHistory.getOrder().getId());
            else ps.setNull(5, Types.BIGINT);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    emailHistory.setId(rs.getLong(1));
                }
            }
        }
        return emailHistory;
    }

    public List<EmailHistory> findAll() throws SQLException {
        return findByFilter(new EmailFilterRequest(), 1, Integer.MAX_VALUE);
    }

    public List<EmailHistory> findByFilter(EmailFilterRequest filter, int page, int size) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM email_history WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (filter.getRecipient() != null && !filter.getRecipient().isBlank()) {
            sql.append(" AND recipient LIKE ?");
            params.add("%" + filter.getRecipient().trim() + "%");
        }
        if (filter.getSubject() != null && !filter.getSubject().isBlank()) {
            sql.append(" AND subject LIKE ?");
            params.add("%" + filter.getSubject().trim() + "%");
        }
        if (filter.getFrom() != null) {
            sql.append(" AND sent_at >= ?");
            params.add(Timestamp.valueOf(filter.getFrom()));
        }
        if (filter.getTo() != null) {
            sql.append(" AND sent_at <= ?");
            params.add(Timestamp.valueOf(filter.getTo()));
        }

        sql.append(" ORDER BY sent_at DESC LIMIT ? OFFSET ?");
        params.add(size);
        params.add((long) (page - 1) * size);

        List<EmailHistory> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }
        return list;
    }

    public boolean deleteById(long id) throws SQLException {
        String sql = "DELETE FROM email_history WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private EmailHistory map(ResultSet rs) throws SQLException {
        EmailHistory emailHistory = EmailHistory.builder()
                .id(rs.getLong("id"))
                .recipient(rs.getString("recipient"))
                .subject(rs.getString("subject"))
                .body(rs.getString("body"))
                .sentAt(rs.getTimestamp("sent_at").toLocalDateTime())
                .build();
        long orderId = rs.getLong("related_order_id");
        if (!rs.wasNull()) {
            Order order = new Order();
            order.setId(orderId);
            emailHistory.setOrder(order);
        }
        return emailHistory;
    }
}

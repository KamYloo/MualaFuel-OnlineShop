package com.example.MualaFuel_Backend.dao;

import com.example.MualaFuel_Backend.dto.request.EmailFilterRequest;
import com.example.MualaFuel_Backend.entity.EmailHistory;
import com.example.MualaFuel_Backend.entity.Order;
import com.example.MualaFuel_Backend.factory.ConnectionFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class EmailHistoryDaoImpl implements EmailHistoryDao {

    private static final String SQL_SELECT = "SELECT * FROM email_history";
    private static final String SQL_COUNT = "SELECT COUNT(*) FROM email_history";
    private static final String PAGINATION_CLAUSE = " ORDER BY sent_at DESC LIMIT ? OFFSET ?";

    @Override
    public EmailHistory save(EmailHistory emailHistory) {
        final String SQL_INSERT = "INSERT INTO email_history (recipient, subject, body, sent_at, related_order_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            conn.setAutoCommit(false);
            ps.setString(1, emailHistory.getRecipient());
            ps.setString(2, emailHistory.getSubject());
            ps.setString(3, emailHistory.getBody());
            ps.setTimestamp(4, Timestamp.valueOf(emailHistory.getSentAt()));
            if (emailHistory.getOrder() != null) {
                ps.setLong(5, emailHistory.getOrder().getId());
            } else {
                ps.setNull(5, Types.BIGINT);
            }

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating email history failed, no rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    emailHistory.setId(rs.getLong(1));
                } else {
                    throw new SQLException("Creating email history failed, no ID obtained.");
                }
            }

            conn.commit();
            return emailHistory;

        } catch (SQLException ex) {
            throw new RuntimeException("Error saving email history", ex);
        }
    }

    @Override
    public Optional<EmailHistory> findById(Long id) {
        final String SQL_FIND = SQL_SELECT + " WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEmailHistory(rs));
                }
            }
            return Optional.empty();

        } catch (SQLException ex) {
            throw new RuntimeException("Error finding email history by id: " + id, ex);
        }
    }

    @Override
    public Page<EmailHistory> findAll(Pageable pageable, EmailFilterRequest filter) {
        StringBuilder sql = new StringBuilder(SQL_SELECT);
        StringBuilder countSql = new StringBuilder(SQL_COUNT);
        List<Object> params = new ArrayList<>();

        buildFilters(filter, sql, countSql, params);

        sql.append(PAGINATION_CLAUSE);

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement dataStmt = conn.prepareStatement(sql.toString())) {

            setParams(dataStmt, params);
            dataStmt.setInt(params.size() + 1, pageable.getPageSize());
            dataStmt.setInt(params.size() + 2, (int) pageable.getOffset());

            List<EmailHistory> list = new ArrayList<>();
            try (ResultSet rs = dataStmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToEmailHistory(rs));
                }
            }

            long total = executeCount(conn, countSql.toString(), params);
            return new PageImpl<>(list, pageable, total);

        } catch (SQLException ex) {
            throw new RuntimeException("Error retrieving email histories", ex);
        }
    }

    @Override
    public void delete(Long id) {
        final String SQL_DELETE = "DELETE FROM email_history WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setLong(1, id);
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new RuntimeException("Deleting email history failed, no rows affected.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error deleting email history with id: " + id, ex);
        }
    }

    private void buildFilters(EmailFilterRequest filter, StringBuilder sql, StringBuilder countSql, List<Object> params) {
        if (filter.getRecipient() != null && !filter.getRecipient().isBlank()) {
            appendWhereOrAnd(sql, params);
            sql.append(" recipient ILIKE ?");
            appendWhereOrAnd(countSql, params);
            countSql.append(" recipient ILIKE ?");
            params.add("%" + filter.getRecipient().trim() + "%");
        }
        if (filter.getSubject() != null && !filter.getSubject().isBlank()) {
            appendWhereOrAnd(sql, params);
            sql.append(" subject ILIKE ?");
            appendWhereOrAnd(countSql, params);
            countSql.append(" subject ILIKE ?");
            params.add("%" + filter.getSubject().trim() + "%");
        }
        if (filter.getFrom() != null) {
            appendWhereOrAnd(sql, params);
            sql.append(" sent_at >= ?");
            appendWhereOrAnd(countSql, params);
            countSql.append(" sent_at >= ?");
            params.add(Timestamp.valueOf(filter.getFrom()));
        }
        if (filter.getTo() != null) {
            appendWhereOrAnd(sql, params);
            sql.append(" sent_at <= ?");
            appendWhereOrAnd(countSql, params);
            countSql.append(" sent_at <= ?");
            params.add(Timestamp.valueOf(filter.getTo()));
        }
        if (filter.getRelatedToOrder() != null) {
            appendWhereOrAnd(sql, params);
            appendWhereOrAnd(countSql, params);

            if (filter.getRelatedToOrder()) {
                sql.append(" related_order_id IS NOT NULL");
                countSql.append(" related_order_id IS NOT NULL");
            } else {
                sql.append(" related_order_id IS NULL");
                countSql.append(" related_order_id IS NULL");
            }
        }
    }

    private void appendWhereOrAnd(StringBuilder sql, List<Object> params) {
        if (params.isEmpty()) {
            sql.append(" WHERE");
        } else {
            sql.append(" AND");
        }
    }

    private long executeCount(Connection conn, String countSql, List<Object> params) throws SQLException {
        try (PreparedStatement countStmt = conn.prepareStatement(countSql)) {
            setParams(countStmt, params);
            try (ResultSet crs = countStmt.executeQuery()) {
                if (crs.next()) {
                    return crs.getLong(1);
                }
            }
        }
        return 0;
    }

    private void setParams(PreparedStatement ps, List<Object> params) {
        try {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error setting query parameters", e);
        }
    }

    private EmailHistory mapResultSetToEmailHistory(ResultSet rs) throws SQLException {
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

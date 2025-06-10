package com.example.MualaFuel_Backend.dao;

import com.example.MualaFuel_Backend.dto.request.EmailFilterRequest;
import com.example.MualaFuel_Backend.entity.EmailHistory;
import com.example.MualaFuel_Backend.entity.Order;
import com.example.MualaFuel_Backend.factory.ConnectionFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailHistoryDaoTest {

    @Mock Connection connection;
    @Mock PreparedStatement preparedStatement;
    @Mock ResultSet resultSet;

    @InjectMocks
    EmailHistoryDaoImpl emailHistoryDao;

    private MockedStatic<ConnectionFactory> connectionFactoryMock;

    EmailHistory sampleEmail;
    Order sampleOrder;

    @BeforeEach
    void setUp() throws Exception {
        sampleOrder = Order.builder().id(10L).build();
        sampleEmail = EmailHistory.builder()
                .id(1L)
                .recipient("test@domain.com")
                .subject("Test subject")
                .body("Test body")
                .sentAt(LocalDateTime.now())
                .order(sampleOrder)
                .build();

        connectionFactoryMock = Mockito.mockStatic(ConnectionFactory.class);
        connectionFactoryMock.when(ConnectionFactory::getConnection).thenReturn(connection);
    }

    @AfterEach
    void tearDown() {
        connectionFactoryMock.close();
    }

    @Test
    void testSaveEmailHistory() throws Exception {
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        ResultSet generatedKeys = mock(ResultSet.class);
        when(preparedStatement.getGeneratedKeys()).thenReturn(generatedKeys);
        when(generatedKeys.next()).thenReturn(true);
        when(generatedKeys.getLong(1)).thenReturn(1L);

        EmailHistory result = emailHistoryDao.save(sampleEmail);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(preparedStatement).setString(1, "test@domain.com");
        verify(preparedStatement).setString(2, "Test subject");
        verify(preparedStatement).setString(3, "Test body");
        verify(preparedStatement, atLeastOnce()).executeUpdate();
        verify(connection).commit();
    }

    @Test
    void testFindByIdReturnsEmailHistory() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("recipient")).thenReturn("test@domain.com");
        when(resultSet.getString("subject")).thenReturn("Test subject");
        when(resultSet.getString("body")).thenReturn("Test body");
        when(resultSet.getTimestamp("sent_at")).thenReturn(Timestamp.valueOf(sampleEmail.getSentAt()));
        when(resultSet.getLong("related_order_id")).thenReturn(10L);
        when(resultSet.wasNull()).thenReturn(false);

        Optional<EmailHistory> result = emailHistoryDao.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("test@domain.com", result.get().getRecipient());
        assertEquals("Test subject", result.get().getSubject());
        assertEquals(10L, result.get().getOrder().getId());
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeQuery();
    }

    @Test
    void testFindByIdReturnsEmpty() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Optional<EmailHistory> result = emailHistoryDao.findById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteEmailHistory() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> emailHistoryDao.delete(1L));
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testFindAllReturnsPage() throws Exception {
        EmailFilterRequest filter = new EmailFilterRequest();
        PageRequest pageable = PageRequest.of(0, 10);

        when(connection.prepareStatement(contains("SELECT * FROM email_history"))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("recipient")).thenReturn("test@domain.com");
        when(resultSet.getString("subject")).thenReturn("Test subject");
        when(resultSet.getString("body")).thenReturn("Test body");
        when(resultSet.getTimestamp("sent_at")).thenReturn(Timestamp.valueOf(sampleEmail.getSentAt()));
        when(resultSet.getLong("related_order_id")).thenReturn(10L);
        when(resultSet.wasNull()).thenReturn(false);

        PreparedStatement countStmt = mock(PreparedStatement.class);
        ResultSet countRs = mock(ResultSet.class);
        when(connection.prepareStatement(contains("COUNT(*)"))).thenReturn(countStmt);
        when(countStmt.executeQuery()).thenReturn(countRs);
        when(countRs.next()).thenReturn(true);
        when(countRs.getLong(1)).thenReturn(1L);

        Page<EmailHistory> page = emailHistoryDao.findAll(pageable, filter);

        assertEquals(1, page.getTotalElements());
        assertEquals(1, page.getContent().size());
        assertEquals("test@domain.com", page.getContent().get(0).getRecipient());
        verify(preparedStatement, atLeastOnce()).executeQuery();
        verify(countStmt, atLeastOnce()).executeQuery();
    }
}

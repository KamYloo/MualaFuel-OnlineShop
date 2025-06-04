package com.example.MualaFuel_Backend.dao;

import com.example.MualaFuel_Backend.entity.AuditEntry;
import com.example.MualaFuel_Backend.enums.AuditLevel;
import com.example.MualaFuel_Backend.factory.ConnectionFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditEntryDaoTest {

    @Mock Connection connection;
    @Mock PreparedStatement preparedStatement;

    @InjectMocks AuditEntryDao auditEntryDao;

    private MockedStatic<ConnectionFactory> connectionFactoryMock;

    AuditEntry sampleEntry;

    @BeforeEach
    void setUp() throws Exception {
        sampleEntry = AuditEntry.builder()
                .id(1L)
                .level(AuditLevel.INFO)
                .eventType("LOGIN")
                .source("system")
                .details("User logged in")
                .createdAt(LocalDateTime.now())
                .build();

        connectionFactoryMock = Mockito.mockStatic(ConnectionFactory.class);
        connectionFactoryMock.when(ConnectionFactory::getConnection).thenReturn(connection);
    }

    @AfterEach
    void tearDown() {
        connectionFactoryMock.close();
    }

    @Test
    void testSaveAuditEntrySuccess() throws Exception {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        assertDoesNotThrow(() -> auditEntryDao.save(sampleEntry));

        verify(connection).setAutoCommit(false);
        verify(preparedStatement).setString(1, "INFO");
        verify(preparedStatement).setString(2, "LOGIN");
        verify(preparedStatement).setString(3, "system");
        verify(preparedStatement).setString(4, "User logged in");
        verify(preparedStatement, atLeastOnce()).setTimestamp(eq(5), any(Timestamp.class));
        verify(preparedStatement).executeUpdate();
        verify(connection).commit();
    }

    @Test
    void testSaveAuditEntryThrowsException() throws Exception {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> auditEntryDao.save(sampleEntry));
        assertTrue(ex.getMessage().contains("Cannot save audit entry"));
    }
}

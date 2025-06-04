package com.example.MualaFuel_Backend.service;

import com.example.MualaFuel_Backend.dao.AuditEntryDao;
import com.example.MualaFuel_Backend.entity.AuditEntry;
import com.example.MualaFuel_Backend.enums.AuditLevel;
import com.example.MualaFuel_Backend.service.impl.AuditServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class AuditServiceTest {

    @Mock
    AuditEntryDao auditEntryDao;

    @InjectMocks
    AuditServiceImpl auditService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogWithDefaultLevel() {
        auditService.log("LOGIN", "system", "User logged in");

        ArgumentCaptor<AuditEntry> captor = ArgumentCaptor.forClass(AuditEntry.class);
        verify(auditEntryDao).save(captor.capture());
        AuditEntry entry = captor.getValue();

        assertEquals(AuditLevel.INFO, entry.getLevel());
        assertEquals("LOGIN", entry.getEventType());
        assertEquals("system", entry.getSource());
        assertEquals("User logged in", entry.getDetails());
        assertNotNull(entry.getCreatedAt());
    }

    @Test
    void testLogWithCustomLevel() {
        auditService.log("LOGIN", AuditLevel.ERROR, "system", "Login failed");

        ArgumentCaptor<AuditEntry> captor = ArgumentCaptor.forClass(AuditEntry.class);
        verify(auditEntryDao).save(captor.capture());
        AuditEntry entry = captor.getValue();

        assertEquals(AuditLevel.ERROR, entry.getLevel());
        assertEquals("LOGIN", entry.getEventType());
        assertEquals("system", entry.getSource());
        assertEquals("Login failed", entry.getDetails());
        assertNotNull(entry.getCreatedAt());
    }
}

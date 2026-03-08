package com.example.MualaFuel_Backend.controller;

import com.example.MualaFuel_Backend.dto.EmailHistoryDto;
import com.example.MualaFuel_Backend.dto.request.EmailFilterRequest;
import com.example.MualaFuel_Backend.service.EmailHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailHistoryControllerTest {

    @Mock
    EmailHistoryService emailHistoryService;

    @InjectMocks
    EmailHistoryController emailHistoryController;

    EmailHistoryDto sampleDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleDto = new EmailHistoryDto(1L, "test@domain.com", "Temat", LocalDateTime.now());
    }

    @Test
    void testGetEmails() {
        Pageable pageable = PageRequest.of(0, 10);
        EmailFilterRequest filter = new EmailFilterRequest();
        Page<EmailHistoryDto> page = new PageImpl<>(List.of(sampleDto));

        when(emailHistoryService.getEmailHistories(pageable, filter)).thenReturn(page);

        ResponseEntity<Page<EmailHistoryDto>> response = emailHistoryController.getEmails(pageable, filter);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(sampleDto, response.getBody().getContent().get(0));
        verify(emailHistoryService).getEmailHistories(pageable, filter);
    }

    @Test
    void testGetEmailBodyById() {
        when(emailHistoryService.getEmailBodyById(1L)).thenReturn("body");

        ResponseEntity<String> response = emailHistoryController.getEmailBodyById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("body", response.getBody());
        verify(emailHistoryService).getEmailBodyById(1L);
    }

    @Test
    void testDeleteEmailHistory() {
        doNothing().when(emailHistoryService).deleteEmailHistory(1L);

        ResponseEntity<?> response = emailHistoryController.deleteEmailHistory(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody());
        verify(emailHistoryService).deleteEmailHistory(1L);
    }
}

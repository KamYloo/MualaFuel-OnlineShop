package com.example.MualaFuel_Backend.service;

import com.example.MualaFuel_Backend.dao.EmailHistoryDaoImpl;
import com.example.MualaFuel_Backend.dto.EmailHistoryDto;
import com.example.MualaFuel_Backend.dto.request.EmailFilterRequest;
import com.example.MualaFuel_Backend.entity.EmailHistory;
import com.example.MualaFuel_Backend.handler.BusinessErrorCodes;
import com.example.MualaFuel_Backend.handler.CustomException;
import com.example.MualaFuel_Backend.mapper.Mapper;
import com.example.MualaFuel_Backend.service.impl.EmailHistoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailHistoryServiceTest {

    @Mock
    EmailHistoryDaoImpl emailHistoryDao;
    @Mock
    Mapper<EmailHistory, EmailHistoryDto> emailHistoryMapper;

    @InjectMocks
    EmailHistoryServiceImpl emailHistoryService;

    EmailHistory sampleEmailHistory;
    EmailHistoryDto sampleEmailHistoryDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleEmailHistory = EmailHistory.builder()
                .id(1L)
                .recipient("test@domain.com")
                .subject("Test subject")
                .body("Test body")
                .sentAt(LocalDateTime.now())
                .build();

        sampleEmailHistoryDto = new EmailHistoryDto(
                1L, "test@domain.com", "Test subject", sampleEmailHistory.getSentAt()
        );
    }

    @Test
    void testSaveEmailHistory() {
        assertDoesNotThrow(() -> emailHistoryService.saveEmailHistory(sampleEmailHistory));
        verify(emailHistoryDao).save(sampleEmailHistory);
    }

    @Test
    void testGetEmailHistoriesReturnsPage() {
        EmailFilterRequest filter = new EmailFilterRequest();
        Pageable pageable = PageRequest.of(0, 10);
        Page<EmailHistory> page = new PageImpl<>(java.util.List.of(sampleEmailHistory));
        when(emailHistoryDao.findAll(pageable, filter)).thenReturn(page);
        when(emailHistoryMapper.mapTo(any(EmailHistory.class))).thenReturn(sampleEmailHistoryDto);

        Page<EmailHistoryDto> result = emailHistoryService.getEmailHistories(pageable, filter);

        assertEquals(1, result.getTotalElements());
        assertEquals("test@domain.com", result.getContent().get(0).getRecipient());
        verify(emailHistoryDao).findAll(pageable, filter);
        verify(emailHistoryMapper).mapTo(sampleEmailHistory);
    }

    @Test
    void testGetEmailBodyByIdReturnsBody() {
        when(emailHistoryDao.findById(1L)).thenReturn(Optional.of(sampleEmailHistory));

        String body = emailHistoryService.getEmailBodyById(1L);

        assertEquals("Test body", body);
        verify(emailHistoryDao).findById(1L);
    }

    @Test
    void testGetEmailBodyByIdThrowsIfNotFound() {
        when(emailHistoryDao.findById(2L)).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class, () -> emailHistoryService.getEmailBodyById(2L));
        assertEquals(BusinessErrorCodes.EMAIL_HISTORY_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void testDeleteEmailHistory() {
        when(emailHistoryDao.findById(1L)).thenReturn(Optional.of(sampleEmailHistory));

        assertDoesNotThrow(() -> emailHistoryService.deleteEmailHistory(1L));
        verify(emailHistoryDao).delete(1L);
    }

    @Test
    void testDeleteEmailHistoryThrowsIfNotFound() {
        when(emailHistoryDao.findById(2L)).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class, () -> emailHistoryService.deleteEmailHistory(2L));
        assertEquals(BusinessErrorCodes.EMAIL_HISTORY_NOT_FOUND, ex.getErrorCode());
    }
}

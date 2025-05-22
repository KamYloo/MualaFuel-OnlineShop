package com.example.MualaFuel_Backend.service;

import com.example.MualaFuel_Backend.dto.EmailHistoryDto;
import com.example.MualaFuel_Backend.dto.request.EmailFilterRequest;
import com.example.MualaFuel_Backend.entity.EmailHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmailHistoryService {
    void saveEmailHistory(EmailHistory emailHistory);
    Page<EmailHistoryDto> getEmailHistories(Pageable pageable, EmailFilterRequest filter);
    String getEmailBodyById(Long id);
    void deleteEmailHistory(Long id);
}

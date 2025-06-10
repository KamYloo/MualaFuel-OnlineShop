package com.example.MualaFuel_Backend.service.impl;

import com.example.MualaFuel_Backend.dao.EmailHistoryDaoImpl;
import com.example.MualaFuel_Backend.dto.EmailHistoryDto;
import com.example.MualaFuel_Backend.dto.request.EmailFilterRequest;
import com.example.MualaFuel_Backend.entity.EmailHistory;
import com.example.MualaFuel_Backend.handler.BusinessErrorCodes;
import com.example.MualaFuel_Backend.handler.CustomException;
import com.example.MualaFuel_Backend.mapper.Mapper;
import com.example.MualaFuel_Backend.service.EmailHistoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailHistoryServiceImpl implements EmailHistoryService {
    private final EmailHistoryDaoImpl emailHistoryRepository;
    private final Mapper<EmailHistory, EmailHistoryDto> emailHistoryMapper;


    @Override
    @Transactional
    public void saveEmailHistory(EmailHistory emailHistory) {
        emailHistoryRepository.save(emailHistory);
    }

    @Override
    public Page<EmailHistoryDto> getEmailHistories(Pageable pageable, EmailFilterRequest filter) {
        return emailHistoryRepository.findAll(pageable,filter).map(emailHistoryMapper::mapTo);
    }

    @Override
    public String getEmailBodyById(Long id) {
        EmailHistory emailHistory = emailHistoryRepository.findById(id).orElseThrow(
                () -> new CustomException(BusinessErrorCodes.EMAIL_HISTORY_NOT_FOUND));
        return emailHistory.getBody();
    }

    @Override
    @Transactional
    public void deleteEmailHistory(Long id) {
        EmailHistory existing = emailHistoryRepository.findById(id).orElseThrow(
                () -> new CustomException(BusinessErrorCodes.EMAIL_HISTORY_NOT_FOUND));
        emailHistoryRepository.delete(existing.getId());
    }
}

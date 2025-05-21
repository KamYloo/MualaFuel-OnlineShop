package com.example.MualaFuel_Backend.service.impl;

import com.example.MualaFuel_Backend.dao.EmailHistoryDao;
import com.example.MualaFuel_Backend.dto.EmailHistoryDto;
import com.example.MualaFuel_Backend.dto.request.EmailFilterRequest;
import com.example.MualaFuel_Backend.entity.EmailHistory;
import com.example.MualaFuel_Backend.mapper.Mapper;
import com.example.MualaFuel_Backend.service.EmailHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailHistoryServiceImpl implements EmailHistoryService {
    private final EmailHistoryDao emailHistoryRepository;
    private final Mapper<EmailHistory, EmailHistoryDto> emailHistoryMapper;


    @Override
    public void saveEmailHistory(EmailHistory emailHistory) {
        emailHistoryRepository.save(emailHistory);
    }

    @Override
    public Page<EmailHistoryDto> getEmailHistories(Pageable pageable, EmailFilterRequest filter) {
        return emailHistoryRepository.findAll(pageable,filter).map(emailHistoryMapper::mapTo);
    }
}

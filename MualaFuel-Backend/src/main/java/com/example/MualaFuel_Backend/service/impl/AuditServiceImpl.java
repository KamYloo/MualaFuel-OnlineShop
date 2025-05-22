package com.example.MualaFuel_Backend.service.impl;

import com.example.MualaFuel_Backend.dao.AuditEntryDao;
import com.example.MualaFuel_Backend.entity.AuditEntry;
import com.example.MualaFuel_Backend.enums.AuditLevel;
import com.example.MualaFuel_Backend.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {
    private final AuditEntryDao auditEntryRepository;

    @Override
    @Transactional
    public void log(String eventType, String source, String details) {
        log(eventType, AuditLevel.INFO, source, details);
    }

    @Override
    @Transactional
    public void log(String eventType, AuditLevel level, String source, String details) {
        AuditEntry entry = AuditEntry.builder()
                .level(level)
                .eventType(eventType)
                .source(source)
                .details(details)
                .createdAt(LocalDateTime.now())
                .build();
        auditEntryRepository.save(entry);
    }
}

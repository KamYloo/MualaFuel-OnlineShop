package com.example.MualaFuel_Backend.service;

import com.example.MualaFuel_Backend.enums.AuditLevel;

public interface AuditService {
    void log(String eventType, String source, String details);
    void log(String eventType, AuditLevel level, String source, String details);
}

package com.example.MualaFuel_Backend.service;

public interface AuditService {
    void log(String eventType, String source, String details);
}

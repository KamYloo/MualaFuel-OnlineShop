package com.example.MualaFuel_Backend.service;

import com.example.MualaFuel_Backend.entity.EmailHistory;

import java.sql.SQLException;

public interface EmailHistoryService {
    void saveEmailHistory(EmailHistory emailHistory) throws SQLException;
}

package com.example.MualaFuel_Backend.dao;

import com.example.MualaFuel_Backend.dto.request.EmailFilterRequest;
import com.example.MualaFuel_Backend.entity.EmailHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmailHistoryDao extends Dao<EmailHistory, Long> {
    Page<EmailHistory> findAll(Pageable pageable, EmailFilterRequest filter);
}

package com.example.MualaFuel_Backend.service.impl;

import com.example.MualaFuel_Backend.dao.EmailHistoryDao;
import com.example.MualaFuel_Backend.entity.EmailHistory;
import com.example.MualaFuel_Backend.service.EmailHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class EmailHistoryServiceImpl implements EmailHistoryService {
    private final EmailHistoryDao emailHistoryDao;


    @Override
    public void saveEmailHistory(EmailHistory emailHistory) throws SQLException {
        emailHistoryDao.save(emailHistory);
    }
}

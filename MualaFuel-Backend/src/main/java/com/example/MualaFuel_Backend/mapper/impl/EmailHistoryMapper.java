package com.example.MualaFuel_Backend.mapper.impl;

import com.example.MualaFuel_Backend.dto.EmailHistoryDto;
import com.example.MualaFuel_Backend.entity.EmailHistory;
import com.example.MualaFuel_Backend.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailHistoryMapper implements Mapper<EmailHistory, EmailHistoryDto>{
    private ModelMapper modelMapper;

    @Override
    public EmailHistoryDto mapTo(EmailHistory emailHistory) {
        return modelMapper.map(emailHistory, EmailHistoryDto.class);
    }

    @Override
    public EmailHistory mapFrom(EmailHistoryDto emailHistoryDto) {return modelMapper.map(emailHistoryDto, EmailHistory.class);}
}
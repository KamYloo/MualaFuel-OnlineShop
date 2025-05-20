package com.example.MualaFuel_Backend.mapper.impl;

import com.example.MualaFuel_Backend.dto.EmailDto;
import com.example.MualaFuel_Backend.entity.Email;
import com.example.MualaFuel_Backend.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailMapper implements Mapper<Email, EmailDto>{
    private ModelMapper modelMapper;

    @Override
    public EmailDto mapTo(Email email) {
        return modelMapper.map(email, EmailDto.class);
    }

    @Override
    public Email mapFrom(EmailDto emailDto) {return modelMapper.map(emailDto, Email.class);}
}
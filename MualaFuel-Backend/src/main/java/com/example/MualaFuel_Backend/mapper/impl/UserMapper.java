package com.example.MualaFuel_Backend.mapper.impl;

import com.example.MualaFuel_Backend.dto.UserDto;
import com.example.MualaFuel_Backend.entity.User;
import com.example.MualaFuel_Backend.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapper implements Mapper<User, UserDto> {

    private ModelMapper modelMapper;

    @Override
    public UserDto mapTo(User userEntity) {
        return modelMapper.map(userEntity, UserDto.class);
    }


    @Override
    public User mapFrom(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
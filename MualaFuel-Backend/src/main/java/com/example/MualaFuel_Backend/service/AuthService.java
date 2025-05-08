package com.example.MualaFuel_Backend.service;


import com.example.MualaFuel_Backend.dto.UserDto;
import com.example.MualaFuel_Backend.dto.request.LoginRequest;
import com.example.MualaFuel_Backend.dto.request.RegisterRequest;
import jakarta.mail.MessagingException;

public interface AuthService {
    UserDto createUser(RegisterRequest registerRequest) throws MessagingException;
    String verify(LoginRequest loginRequest) throws MessagingException;
    UserDto findUserByEmail(String email);
    UserDto verifyToken(String token);
}

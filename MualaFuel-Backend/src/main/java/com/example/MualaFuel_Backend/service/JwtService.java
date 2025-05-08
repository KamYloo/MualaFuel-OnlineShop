package com.example.MualaFuel_Backend.service;

import com.example.MualaFuel_Backend.handler.CustomException;

public interface JwtService {
    String generateToken(String username);
    String extractUserName(String token);
    boolean validateJwtToken(String authToken) throws CustomException;
}

package com.example.MualaFuel_Backend.controller;

import com.example.MualaFuel_Backend.dto.UserDto;
import com.example.MualaFuel_Backend.dto.request.LoginRequest;
import com.example.MualaFuel_Backend.dto.request.RegisterRequest;
import com.example.MualaFuel_Backend.service.AuthService;
import com.example.MualaFuel_Backend.service.CookieService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieService cookieService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest) throws MessagingException {
        UserDto createdUser = authService.createUser(registerRequest);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response)
            throws MessagingException {
        String token = authService.verify(loginRequest);
        response.addCookie(cookieService.getNewCookie("jwt", token));
        UserDto userDto = authService.findUserByEmail(loginRequest.getEmail());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        response.addCookie(cookieService.deleteCookie("jwt"));
        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }
}

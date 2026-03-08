package com.example.MualaFuel_Backend.controller;

import com.example.MualaFuel_Backend.dto.UserDto;
import com.example.MualaFuel_Backend.dto.request.LoginRequest;
import com.example.MualaFuel_Backend.dto.request.RegisterRequest;
import com.example.MualaFuel_Backend.service.AuthService;
import com.example.MualaFuel_Backend.service.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Mock AuthService authService;
    @Mock CookieService cookieService;
    @Mock HttpServletRequest httpServletRequest;
    @Mock HttpServletResponse httpServletResponse;

    @InjectMocks AuthController authController;

    UserDto sampleUserDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleUserDto = UserDto.builder()
                .id(1L)
                .firstName("Jan")
                .lastName("Kowalski")
                .email("jan@kowalski.pl")
                .build();
    }

    @Test
    void testRegister() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .email("jan@kowalski.pl")
                .password("pass1234")
                .build();

        when(authService.createUser(registerRequest)).thenReturn(sampleUserDto);

        ResponseEntity<?> response = authController.register(registerRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleUserDto, response.getBody());
        verify(authService).createUser(registerRequest);
    }

    @Test
    void testLogin() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("jan@kowalski.pl")
                .password("pass1234")
                .build();

        String token = "jwt.token";
        Cookie cookie = new Cookie("jwt", token);

        when(authService.verify(loginRequest)).thenReturn(token);
        when(cookieService.getNewCookie("jwt", token)).thenReturn(cookie);
        when(authService.findUserByEmail("jan@kowalski.pl")).thenReturn(sampleUserDto);

        ResponseEntity<?> response = authController.login(loginRequest, httpServletResponse);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleUserDto, response.getBody());
        verify(httpServletResponse).addCookie(cookie);
        verify(authService).verify(loginRequest);
        verify(authService).findUserByEmail("jan@kowalski.pl");
    }

    @Test
    void testLogout() {
        Cookie cookie = new Cookie("jwt", null);
        when(cookieService.deleteCookie("jwt")).thenReturn(cookie);

        ResponseEntity<?> response = authController.logout(httpServletRequest, httpServletResponse);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Logged out successfully", response.getBody());
        verify(httpServletResponse).addCookie(cookie);
    }

    @Test
    void testCheck() {
        String token = "jwt.token";
        when(cookieService.getJwtCookie(httpServletRequest)).thenReturn(token);
        when(authService.verifyToken(token)).thenReturn(sampleUserDto);

        ResponseEntity<?> response = authController.check(httpServletRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleUserDto, response.getBody());
        verify(cookieService).getJwtCookie(httpServletRequest);
        verify(authService).verifyToken(token);
    }
}

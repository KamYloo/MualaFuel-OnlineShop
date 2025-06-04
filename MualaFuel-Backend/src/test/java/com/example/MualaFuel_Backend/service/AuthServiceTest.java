package com.example.MualaFuel_Backend.service;

import com.example.MualaFuel_Backend.dao.RoleDao;
import com.example.MualaFuel_Backend.dao.UserDao;
import com.example.MualaFuel_Backend.dto.UserDto;
import com.example.MualaFuel_Backend.dto.request.LoginRequest;
import com.example.MualaFuel_Backend.dto.request.RegisterRequest;
import com.example.MualaFuel_Backend.entity.Role;
import com.example.MualaFuel_Backend.entity.User;
import com.example.MualaFuel_Backend.handler.BusinessErrorCodes;
import com.example.MualaFuel_Backend.handler.CustomException;
import com.example.MualaFuel_Backend.mapper.Mapper;
import com.example.MualaFuel_Backend.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock UserDao userDao;
    @Mock RoleDao roleDao;
    @Mock PasswordEncoder passwordEncoder;
    @Mock AuthenticationManager authenticationManager;
    @Mock Mapper<User, UserDto> mapper;
    @Mock JwtService jwtService;

    @InjectMocks AuthServiceImpl authService;

    User sampleUser;
    UserDto sampleUserDto;
    Role sampleRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleRole = Role.builder().id(1L).name("USER").build();
        sampleUser = User.builder()
                .id(1L)
                .firstName("Jan")
                .lastName("Kowalski")
                .email("jan@kowalski.pl")
                .password("hashed")
                .roles(Set.of(sampleRole))
                .build();
        sampleUserDto = UserDto.builder()
                .id(1L)
                .firstName("Jan")
                .lastName("Kowalski")
                .email("jan@kowalski.pl")
                .roles(Set.of(sampleRole))
                .build();
    }

    @Test
    void testCreateUserSuccess() {
        RegisterRequest req = RegisterRequest.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .email("jan@kowalski.pl")
                .password("pass1234")
                .build();

        when(userDao.findByEmail("jan@kowalski.pl")).thenReturn(Optional.empty());
        when(roleDao.findByName("USER")).thenReturn(Optional.of(sampleRole));
        when(passwordEncoder.encode("pass1234")).thenReturn("hashed");
        when(userDao.save(any(User.class))).thenReturn(sampleUser);
        when(mapper.mapTo(any(User.class))).thenReturn(sampleUserDto);

        UserDto result = authService.createUser(req);

        assertNotNull(result);
        assertEquals("jan@kowalski.pl", result.getEmail());
        verify(userDao).save(any(User.class));
    }

    @Test
    void testCreateUserThrowsIfEmailUsed() {
        RegisterRequest req = RegisterRequest.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .email("jan@kowalski.pl")
                .password("pass1234")
                .build();

        when(userDao.findByEmail("jan@kowalski.pl")).thenReturn(Optional.of(sampleUser));

        CustomException ex = assertThrows(CustomException.class, () -> authService.createUser(req));
        assertEquals(BusinessErrorCodes.EMAIL_IS_USED, ex.getErrorCode());
    }

    @Test
    void testCreateUserThrowsIfRoleNotFound() {
        RegisterRequest req = RegisterRequest.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .email("jan@kowalski.pl")
                .password("pass1234")
                .build();

        when(userDao.findByEmail("jan@kowalski.pl")).thenReturn(Optional.empty());
        when(roleDao.findByName("USER")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.createUser(req));
        assertEquals("Role not found", ex.getMessage());
    }

    @Test
    void testVerifySuccess() {
        LoginRequest req = LoginRequest.builder()
                .email("jan@kowalski.pl")
                .password("pass1234")
                .build();

        when(userDao.findByEmail("jan@kowalski.pl")).thenReturn(Optional.of(sampleUser));
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(auth.isAuthenticated()).thenReturn(true);
        when(jwtService.generateToken("jan@kowalski.pl")).thenReturn("jwt.token");

        String token = authService.verify(req);

        assertEquals("jwt.token", token);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testVerifyThrowsIfUserNotFound() {
        LoginRequest req = LoginRequest.builder()
                .email("notfound@domain.com")
                .password("pass1234")
                .build();

        when(userDao.findByEmail("notfound@domain.com")).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class, () -> authService.verify(req));
        assertEquals(BusinessErrorCodes.BAD_CREDENTIALS, ex.getErrorCode());
    }

    @Test
    void testVerifyThrowsIfNotAuthenticated() {
        LoginRequest req = LoginRequest.builder()
                .email("jan@kowalski.pl")
                .password("pass1234")
                .build();

        when(userDao.findByEmail("jan@kowalski.pl")).thenReturn(Optional.of(sampleUser));
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(auth.isAuthenticated()).thenReturn(false);

        CustomException ex = assertThrows(CustomException.class, () -> authService.verify(req));
        assertEquals(BusinessErrorCodes.BAD_CREDENTIALS, ex.getErrorCode());
    }

    @Test
    void testFindUserByEmailSuccess() {
        when(userDao.findByEmail("jan@kowalski.pl")).thenReturn(Optional.of(sampleUser));
        when(mapper.mapTo(sampleUser)).thenReturn(sampleUserDto);

        UserDto result = authService.findUserByEmail("jan@kowalski.pl");

        assertNotNull(result);
        assertEquals("jan@kowalski.pl", result.getEmail());
    }

    @Test
    void testFindUserByEmailThrowsIfNotFound() {
        when(userDao.findByEmail("notfound@domain.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authService.findUserByEmail("notfound@domain.com"));
    }

    @Test
    void testVerifyTokenSuccess() {
        when(jwtService.validateJwtToken("token")).thenReturn(true);
        when(jwtService.extractUserName("token")).thenReturn("jan@kowalski.pl");
        when(userDao.findByEmail("jan@kowalski.pl")).thenReturn(Optional.of(sampleUser));
        when(mapper.mapTo(sampleUser)).thenReturn(sampleUserDto);

        UserDto result = authService.verifyToken("token");

        assertNotNull(result);
        assertEquals("jan@kowalski.pl", result.getEmail());
    }

    @Test
    void testVerifyTokenThrowsIfInvalidToken() {
        when(jwtService.validateJwtToken("badtoken")).thenReturn(false);

        CustomException ex = assertThrows(CustomException.class, () -> authService.verifyToken("badtoken"));
        assertEquals(BusinessErrorCodes.INVALID_TOKEN, ex.getErrorCode());
    }

    @Test
    void testVerifyTokenThrowsIfUserNotFound() {
        when(jwtService.validateJwtToken("token")).thenReturn(true);
        when(jwtService.extractUserName("token")).thenReturn("notfound@domain.com");
        when(userDao.findByEmail("notfound@domain.com")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.verifyToken("token"));
        assertEquals("User not found", ex.getMessage());
    }
}

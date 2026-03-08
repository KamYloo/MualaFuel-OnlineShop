package com.example.MualaFuel_Backend.service;

import com.example.MualaFuel_Backend.dao.UserDao;
import com.example.MualaFuel_Backend.entity.User;
import com.example.MualaFuel_Backend.service.impl.UserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceTest {

    @Mock
    UserDao userDao;

    @InjectMocks
    UserDetailsService userDetailsService;

    User sampleUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleUser = User.builder()
                .id(1L)
                .email("test@domain.com")
                .password("pass")
                .build();
    }

    @Test
    void testLoadUserByUsernameReturnsUser() {
        when(userDao.findByEmail("test@domain.com")).thenReturn(Optional.of(sampleUser));

        UserDetails result = userDetailsService.loadUserByUsername("test@domain.com");

        assertNotNull(result);
        assertEquals("test@domain.com", result.getUsername());
        verify(userDao).findByEmail("test@domain.com");
    }

    @Test
    void testLoadUserByUsernameThrowsIfNotFound() {
        when(userDao.findByEmail("notfound@domain.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("notfound@domain.com"));
        verify(userDao).findByEmail("notfound@domain.com");
    }
}

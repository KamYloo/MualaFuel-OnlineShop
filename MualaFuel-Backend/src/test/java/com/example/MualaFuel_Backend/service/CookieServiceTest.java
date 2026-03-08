package com.example.MualaFuel_Backend.service;

import com.example.MualaFuel_Backend.handler.BusinessErrorCodes;
import com.example.MualaFuel_Backend.handler.CustomException;
import com.example.MualaFuel_Backend.service.impl.CookieServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CookieServiceTest {

    CookieServiceImpl cookieService;

    @BeforeEach
    void setUp() {
        cookieService = new CookieServiceImpl();
    }

    @Test
    void testGetNewCookie() {
        Cookie cookie = cookieService.getNewCookie("jwt", "token123");
        assertEquals("jwt", cookie.getName());
        assertEquals("token123", cookie.getValue());
        assertTrue(cookie.isHttpOnly());
        assertFalse(cookie.getSecure());
        assertEquals("/", cookie.getPath());
        assertEquals(24 * 60 * 60, cookie.getMaxAge());
    }

    @Test
    void testDeleteCookie() {
        Cookie cookie = cookieService.deleteCookie("jwt");
        assertEquals("jwt", cookie.getName());
        assertNull(cookie.getValue());
        assertTrue(cookie.isHttpOnly());
        assertFalse(cookie.getSecure());
        assertEquals("/", cookie.getPath());
        assertEquals(0, cookie.getMaxAge());
    }

    @Test
    void testGetJwtCookieReturnsValue() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie[] cookies = { new Cookie("jwt", "token123"), new Cookie("other", "val") };
        when(request.getCookies()).thenReturn(cookies);

        String jwt = cookieService.getJwtCookie(request);

        assertEquals("token123", jwt);
    }

    @Test
    void testGetJwtCookieThrowsIfNotFound() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie[] cookies = { new Cookie("other", "val") };
        when(request.getCookies()).thenReturn(cookies);

        CustomException ex = assertThrows(CustomException.class, () -> cookieService.getJwtCookie(request));
        assertEquals(BusinessErrorCodes.BAD_COOKIE, ex.getErrorCode());
    }

    @Test
    void testGetJwtCookieThrowsIfNoCookies() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(null);

        CustomException ex = assertThrows(CustomException.class, () -> cookieService.getJwtCookie(request));
        assertEquals(BusinessErrorCodes.BAD_COOKIE, ex.getErrorCode());
    }
}

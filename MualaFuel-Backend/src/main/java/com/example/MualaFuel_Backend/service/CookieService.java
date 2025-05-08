package com.example.MualaFuel_Backend.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public interface CookieService {
    Cookie getNewCookie(String arg, String value);
    Cookie deleteCookie(String arg);
    String getJwtCookie(HttpServletRequest request);
}

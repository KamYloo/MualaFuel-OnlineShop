package com.example.MualaFuel_Backend.service.impl;

import com.example.MualaFuel_Backend.handler.BusinessErrorCodes;
import com.example.MualaFuel_Backend.handler.CustomException;
import com.example.MualaFuel_Backend.service.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class CookieServiceImpl implements CookieService {
    @Override
    public Cookie getNewCookie(String arg, String value) {
        Cookie cookie = new Cookie(arg, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        return cookie;
    }

    @Override
    public Cookie deleteCookie(String arg) {
        Cookie cookie = new Cookie(arg, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }

    @Override
    public String getJwtCookie(HttpServletRequest request){
        String jwt = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    jwt = cookie.getValue();
                    return jwt;
                }
            }
        }
        throw new CustomException(BusinessErrorCodes.BAD_COOKIE);
    }
}

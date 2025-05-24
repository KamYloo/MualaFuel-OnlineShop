package com.example.MualaFuel_Backend.aspect;

import com.example.MualaFuel_Backend.enums.AuditLevel;
import com.example.MualaFuel_Backend.service.AuditService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CookieAuditAspect {

    private final AuditService auditService;

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.CookieServiceImpl.getNewCookie(..))",
            returning = "cookie")
    public void logCookieCreated(JoinPoint jp, Cookie cookie) {
        auditService.log(
                "COOKIE_CREATED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "name=" + cookie.getName()
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.CookieServiceImpl.deleteCookie(..))",
            returning = "cookie")
    public void logCookieDeleted(JoinPoint jp, Cookie cookie) {
        auditService.log(
                "COOKIE_DELETED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "name=" + cookie.getName()
        );
    }

    @AfterThrowing(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.CookieServiceImpl.getJwtCookie(..))",
            throwing = "ex")
    public void logCookieReadError(JoinPoint jp, Throwable ex) {
        auditService.log(
                "COOKIE_READ_ERROR",
                AuditLevel.ERROR,
                jp.getSignature().toShortString(),
                "error=" + ex.getClass().getSimpleName()
        );
    }
}

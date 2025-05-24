package com.example.MualaFuel_Backend.aspect;

import com.example.MualaFuel_Backend.enums.AuditLevel;
import com.example.MualaFuel_Backend.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthenticationAuditAspect {

    private final AuditService auditService;

    @Before("execution(* com.example.MualaFuel_Backend.service.impl.UserDetailsService.loadUserByUsername(..)) && args(username)")
    public void logAuthAttempt(JoinPoint jp, String username) {
        auditService.log(
                "AUTH_ATTEMPT",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "username=" + username
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.UserDetailsService.loadUserByUsername(..))",
            returning = "userDetails")
    public void logAuthSuccess(JoinPoint jp, Object userDetails) {
        auditService.log(
                "AUTH_SUCCESS",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "user=" + userDetails.toString()
        );
    }

    @AfterThrowing(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.UserDetailsService.loadUserByUsername(..))",
            throwing = "ex")
    public void logAuthFailure(JoinPoint jp, Throwable ex) {
        auditService.log(
                "AUTH_FAILURE",
                AuditLevel.WARN,
                jp.getSignature().toShortString(),
                "error=" + ex.getClass().getSimpleName()
                        + ": " + ex.getMessage()
        );
    }
}

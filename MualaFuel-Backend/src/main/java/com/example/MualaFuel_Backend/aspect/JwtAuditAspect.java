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
public class JwtAuditAspect {

    private final AuditService auditService;

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.JwtServiceImpl.generateToken(..)) && args(username)",
            returning = "token")
    public void logTokenIssued(JoinPoint jp, String username, String token) {
        auditService.log(
                "JWT_ISSUED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "subject=" + username
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.JwtServiceImpl.validateJwtToken(..)) && args(token)",
            returning = "valid")
    public void logTokenValid(JoinPoint jp, String token, boolean valid) {
        auditService.log(
                valid ? "JWT_VALID" : "JWT_INVALID",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "valid=" + valid
        );
    }

    @AfterThrowing(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.JwtServiceImpl.validateJwtToken(..))",
            throwing = "ex")
    public void logTokenError(JoinPoint jp, Throwable ex) {
        auditService.log(
                "JWT_ERROR",
                AuditLevel.ERROR,
                jp.getSignature().toShortString(),
                "error=" + ex.getClass().getSimpleName()
        );
    }
}

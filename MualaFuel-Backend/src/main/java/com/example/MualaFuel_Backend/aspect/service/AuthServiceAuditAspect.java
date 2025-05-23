package com.example.MualaFuel_Backend.aspect.service;

import com.example.MualaFuel_Backend.dto.UserDto;
import com.example.MualaFuel_Backend.enums.AuditLevel;
import com.example.MualaFuel_Backend.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthServiceAuditAspect {

    private final AuditService auditService;

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.AuthServiceImpl.createUser(..))",
            returning = "userDto")
    public void logRegister(JoinPoint jp, Object userDto) {
        UserDto u = (UserDto) userDto;
        auditService.log(
                "USER_REGISTERED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "id=" + u.getId() + ", email=" + u.getEmail()
        );
    }

    @After("execution(* com.example.MualaFuel_Backend.service.impl.AuthServiceImpl.verify(..))")
    public void logLogin(JoinPoint jp) {
        String email = jp.getArgs()[0].toString();
        auditService.log(
                "USER_LOGGED_IN",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "email=" + email
        );
    }


    @AfterThrowing(
            pointcut = "execution(* com.example.MualaFuel_Backend.service.impl.AuthServiceImpl.*(..))",
            throwing = "ex")
    public void logError(JoinPoint jp, Throwable ex) {
        String args = Arrays.stream(jp.getArgs())
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        auditService.log(
                "AUTH_SERVICE_ERROR",
                AuditLevel.ERROR,
                jp.getSignature().toShortString(),
                "args=[" + args + "], error=" + ex.getClass().getSimpleName()
        );
    }
}

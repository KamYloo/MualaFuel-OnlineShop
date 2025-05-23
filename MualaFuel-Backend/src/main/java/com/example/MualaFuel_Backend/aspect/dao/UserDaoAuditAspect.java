package com.example.MualaFuel_Backend.aspect.dao;

import com.example.MualaFuel_Backend.entity.User;
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
public class UserDaoAuditAspect {

    private final AuditService auditService;

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.dao.UserDao.save(..))",
            returning = "saved")
    public void logSave(JoinPoint jp, Object saved) {
        User u = (User) saved;
        auditService.log(
                "USER_CREATED",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "id=" + u.getId() + ", email=" + u.getEmail()
        );
    }

    @AfterReturning(
            pointcut = "execution(* com.example.MualaFuel_Backend.dao.UserDao.findBy*(..))",
            returning = "optUser")
    public void logFind(JoinPoint jp, Object optUser) {
        String args = Arrays.stream(jp.getArgs())
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        boolean isPresent = optUser != null && ((java.util.Optional<?>) optUser).isPresent();
        auditService.log(
                "USER_LOOKUP",
                AuditLevel.INFO,
                jp.getSignature().toShortString(),
                "args=[" + args + "], found=" + isPresent
        );
    }


    @AfterThrowing(
            pointcut = "execution(* com.example.MualaFuel_Backend.dao.UserDao.*(..))",
            throwing = "ex")
    public void logError(JoinPoint jp, Throwable ex) {
        String args = Arrays.stream(jp.getArgs())
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        auditService.log(
                "USER_DAO_ERROR",
                AuditLevel.ERROR,
                jp.getSignature().toShortString(),
                "args=[" + args + "], error=" + ex.getClass().getSimpleName()
        );
    }
}
